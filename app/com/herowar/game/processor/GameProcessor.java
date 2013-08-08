package com.herowar.game.processor;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import play.Logger;

import com.herowar.game.Clock;
import com.herowar.game.models.TowerModel;
import com.herowar.game.models.TowerRestriction;
import com.herowar.game.models.UnitModel;
import com.herowar.game.network.BasePacket;
import com.herowar.game.network.Connection;
import com.herowar.game.network.server.PreloadDataPacket;
import com.herowar.game.network.server.TowerBuildPacket;
import com.herowar.game.network.server.TowerTargetPacket;
import com.herowar.game.network.server.UnitInPacket;
import com.herowar.game.processor.meta.AbstractProcessor;
import com.herowar.game.processor.meta.IPlugin;
import com.herowar.game.processor.meta.IProcessor;
import com.herowar.game.processor.plugin.FinishPlugin;
import com.herowar.game.processor.plugin.GoldPlugin;
import com.herowar.game.processor.plugin.PreloadPlugin;
import com.herowar.game.processor.plugin.TowerPlugin;
import com.herowar.game.processor.plugin.TutorialPlugin;
import com.herowar.game.processor.plugin.UnitPlugin;
import com.herowar.game.processor.plugin.WavePlugin;
import com.herowar.models.entity.game.Map;
import com.herowar.models.entity.game.Match;
import com.herowar.models.entity.game.Vector3;
import com.ssachtleben.play.plugin.event.EventBinding;
import com.ssachtleben.play.plugin.event.Events;

/**
 * The GameProcessor handles every game specific actions like updating player gold, control waves, tower damage and much more.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class GameProcessor extends AbstractProcessor implements IProcessor {

	private final static Logger.ALogger log = Logger.of(GameProcessor.class);

	private GameData data;

	private long gameId;
	private long objectId = 0l;
	private Match match;
	private Map map;

	private Clock clock = new Clock();
	private PreloadDataPacket preloadPacket = null;
	private EventBinding eventBinding;

	private boolean wavesFinished = false;
	private boolean unitsFinished = false;
	private boolean waveRequest = false;
	private boolean tutorialUpdate = false;
	private boolean updateGold = false;
	private State state;

	/**
	 * The {@link Set} contains a list of connections in this game.
	 */
	private Set<Connection> connections = Collections.synchronizedSet(new HashSet<Connection>());

	/**
	 * The plugins map contains all running plugins for each game state.
	 */
	private java.util.Map<State, Set<IPlugin>> plugins = new HashMap<State, Set<IPlugin>>();

	/**
	 * Contains all active {@link UnitModel} in this game.
	 */
	private Set<UnitModel> units = Collections.synchronizedSet(new HashSet<UnitModel>());

	/**
	 * The player cache contains player specific variables and properties like gold and is used by plugins.
	 */
	private ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> playerCache = new ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>>();

	/**
	 * The tower cache contains all placed towers on the map by any player.
	 */
	private ConcurrentHashMap<Long, TowerModel> towerCache = new ConcurrentHashMap<Long, TowerModel>();

	/**
	 * Contains a {@link Set} of {@link TowerRestriction}.
	 */
	private Set<TowerRestriction> towerRestrictions = Collections.synchronizedSet(new HashSet<TowerRestriction>());

	/**
	 * Creates new {@link GameProcessor} instance.
	 * 
	 * @param match
	 */
	public GameProcessor(Match match) {
		super("match-" + match.getId());
		this.data = new GameData(match);
		this.gameId = match.getId();
		this.match = match;
		this.map = match.getMap();
		this.registerPlugins();
		this.updateState(State.PRELOAD);
		this.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.processor.meta.AbstractProcessor#process()
	 */
	@Override
	public void process() {
		double delta = clock.delta();
		long now = clock.currentTime();
		synchronized (plugins) {
			try {
				Iterator<IPlugin> iter = plugins.get(state).iterator();
				while (iter.hasNext()) {
					IPlugin plugin = iter.next();
					plugin.process(delta, now);
				}
				checkGameState();
			} catch (Exception e) {
				log.error("Unexpected Error during game process occured:", e);
				log.error("Stopping " + this.toString());
				shutdown();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.processor.meta.AbstractProcessor#getUpdateTimer()
	 */
	@Override
	public int getUpdateTimer() {
		return 150;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.processor.meta.AbstractProcessor#start()
	 */
	@Override
	public void start() {
		// Register dynamically new game state topic to fetch state change events. This is ugly at moment but there is no proper solution for
		// this. We need to use the eventbus for game related communications.
		try {
			eventBinding = Events.instance().register(getTopicName(Topic.STATE), this, this.getClass().getMethod("updateState", State.class));
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("Failed to register observer", e);
		}
		log.debug(this.toString() + " started");
		super.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.processor.meta.AbstractProcessor#stop()
	 */
	@Override
	public void stop() {
		// Unregister dynamically created event topic subscriber.
		if (eventBinding != null) {
			Events.instance().unregister(getTopicName(Topic.STATE), eventBinding);
		}
		log.debug(this.toString() + " stopped");
		super.stop();
	}

	/**
	 * Shutdown current {@link GameProcessor} thread. And close all running connections.
	 */
	private void shutdown() {
		synchronized (connections) {
			Iterator<Connection> iter = connections.iterator();
			while (iter.hasNext()) {
				Connection connection = iter.next();
				connection.close();
			}
		}
		stop();
	}

	/**
	 * Add a new {@link Connection} to this {@link GameProcessor}.
	 * 
	 * @param connection
	 *          The connection to set
	 */
	public void add(Connection connection) {
		synchronized (connections) {
			connections.add(connection);
		}

		// Change connection on tower if player reconnects
		synchronized (towerCache) {
			Iterator<TowerModel> iter = towerCache.values().iterator();
			while (iter.hasNext()) {
				TowerModel model = iter.next();
				if (model.getConnection().id() == connection.id()) {
					model.setConnection(connection);
				}
			}
		}

		// Create score and kills for player if not exists
		if (!playerCache.containsKey(connection.id())) {
			playerCache.put(connection.id(), new ConcurrentHashMap<String, Object>());
			playerCache.get(connection.id()).put(CacheConstants.SCORE, 0L);
			playerCache.get(connection.id()).put(CacheConstants.KILLS, 0L);
		}

		// Add the session to all plugins
		for (Set<IPlugin> statePlugins : plugins.values()) {
			for (IPlugin plugin : statePlugins) {
				plugin.add(connection);
			}
		}
	}

	/**
	 * Remove a {@link Connection} from this {@link GameProcessor}.
	 * 
	 * @param connection
	 *          The connection to set
	 */
	public void remove(Connection connection) {
		Connection con = null;
		synchronized (connections) {
			Iterator<Connection> iter = connections.iterator();
			while (iter.hasNext()) {
				con = iter.next();
				if (con.equals(connection)) {
					// rootNode.detachChild(player.getModel());
					iter.remove();
					return;
				}
			}
		}
		for (Set<IPlugin> statePlugins : plugins.values()) {
			for (IPlugin plugin : statePlugins) {
				plugin.remove(connection);
			}
		}
	}

	/**
	 * Broadcast a packet to all clients. Its possible to set if the trigger connection should also retrieve the message.
	 * 
	 * @param connection
	 *          The connection to set
	 * @param message
	 *          The message to set
	 * @param sendSelf
	 *          The sendSelf to set
	 */
	public void broadcast(Connection connection, BasePacket message, boolean sendSelf) {
		Iterator<Connection> iter = connections.iterator();
		while (iter.hasNext()) {
			Connection con = iter.next();
			if (!con.preloading() && (sendSelf || !connection.id().equals(con.id()))) {
				con.send(message);
			}
		}
	}

	/**
	 * Broadcast a packet to all clients.
	 * 
	 * @param message
	 *          The message to set
	 */
	public void broadcast(BasePacket message) {
		broadcast(null, message, true);
	}

	/**
	 * Syncronize the player with the current game szenario.
	 * 
	 * @param session
	 *          The session to set
	 */
	public void syncronize(Connection connection) {
		log.info("Start sycronizing for player " + connection.id());
		Iterator<UnitModel> iter = connection.game().getUnits().iterator();
		while (iter.hasNext()) {
			UnitModel unit = iter.next();
			log.info("Send player info about unit " + unit.getId() + " at " + unit.getTranslation().toString());
			connection.send(new UnitInPacket(unit));
		}
		Iterator<TowerModel> iter2 = connection.game().getTowerCache().values().iterator();
		while (iter2.hasNext()) {
			TowerModel tower = iter2.next();
			Vector3 position = new Vector3();
			position.setX(tower.getTranslation().getX());
			position.setY(tower.getTranslation().getY());
			position.setZ(tower.getTranslation().getZ());
			log.info("Send player info about tower " + tower.getId() + " at " + position.toString());
			connection.send(new TowerBuildPacket(tower, position));
			if (tower.getTarget() != null) {
				log.info("Send player info about tower " + tower.getId() + " target unit " + tower.getTarget().getId());
				connection.send(new TowerTargetPacket(tower.getId(), tower.getTarget().getId()));
			}
		}
		log.info("Finish sycronizing for player " + connection.id());
	}

	private void registerPlugins() {
		for (State state : State.values()) {
			plugins.put(state, new HashSet<IPlugin>());
		}
		plugins.get(State.PRELOAD).add(new PreloadPlugin(this));
		plugins.get(State.GAME).add(new GoldPlugin(this));
		plugins.get(State.GAME).add(new TowerPlugin(this));
		plugins.get(State.GAME).add(new UnitPlugin(this));
		plugins.get(State.GAME).add(new WavePlugin(this));
		if ("Tutorial".equals(getMap().getName())) {
			plugins.get(State.GAME).add(new TutorialPlugin(this));
		}
		plugins.get(State.FINISH).add(new FinishPlugin(this));
	}

	private void checkGameState() {
		if (getState().equals(State.GAME) && (getMap().getLives() <= 0 || (isWavesFinished() && isUnitsFinished()))) {
			updateState(State.FINISH);
		}
	}

	public void updateState(State state) {
		if (this.state != null) {
			for (IPlugin plugin : plugins.get(this.state)) {
				plugin.unload();
			}
		}
		if (state != null) {
			for (IPlugin plugin : plugins.get(state)) {
				plugin.load();
			}
		}
		this.state = state;
	}

	public void publish(Topic topic, Object payload) {
		publish(topic, new Object[] { payload });
	}

	public void publish(Topic topic, Object... payload) {
		Events.instance().publish(getTopicName(topic), payload);
	}

	public String getTopicName(Topic topic) {
		return getTopicName() + "-" + topic.name().toLowerCase();
	}

	public enum Topic {
		STATE, PRELOAD, UNIT
	}

	@Override
	public String toString() {
		return "GameProcessor-" + gameId;
	}

	// GETTER && SETTER //

	public GameData data() {
		return data;
	}

	public long getGameId() {
		return gameId;
	}

	public synchronized long getNextObjectId() {
		return objectId++;
	}

	public Set<Connection> getConnections() {
		return connections;
	}

	public java.util.Map<State, Set<IPlugin>> getPlugins() {
		return plugins;
	}

	public ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> getPlayerCache() {
		return playerCache;
	}

	public ConcurrentHashMap<Long, TowerModel> getTowerCache() {
		return towerCache;
	}

	public Match getMatch() {
		return match;
	}

	public Map getMap() {
		return map;
	}

	public State getState() {
		return state;
	}

	public void addUnit(UnitModel unit) {
		this.units.add(unit);
	}

	public Set<UnitModel> getUnits() {
		return units;
	}

	public boolean isWavesFinished() {
		return wavesFinished;
	}

	public void setWavesFinished(boolean wavesFinished) {
		this.wavesFinished = wavesFinished;
	}

	public boolean isUnitsFinished() {
		return unitsFinished;
	}

	public void setUnitsFinished(boolean unitsFinished) {
		this.unitsFinished = unitsFinished;
	}

	public boolean isWaveRequest() {
		return waveRequest;
	}

	public void setWaveRequest(boolean waveRequest) {
		this.waveRequest = waveRequest;
	}

	public boolean isUpdateGold() {
		return updateGold;
	}

	public void setUpdateGold(boolean updateGold) {
		this.updateGold = updateGold;
	}

	public PreloadDataPacket getPreloadPacket() {
		return preloadPacket;
	}

	public void setPreloadPacket(PreloadDataPacket preloadPacket) {
		this.preloadPacket = preloadPacket;
	}

	public boolean isTutorialUpdate() {
		return tutorialUpdate;
	}

	public void setTutorialUpdate(boolean tutorialUpdate) {
		this.tutorialUpdate = tutorialUpdate;
	}

	public Set<TowerRestriction> getTowerRestrictions() {
		return towerRestrictions;
	}

	public void setTowerRestrictions(Set<TowerRestriction> towerRestrictions) {
		this.towerRestrictions = towerRestrictions;
	}

	public enum State {
		PRELOAD, GAME, FINISH
	}
}
