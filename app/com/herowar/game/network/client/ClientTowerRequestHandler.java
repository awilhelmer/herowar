package com.herowar.game.network.client;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.game.models.TowerModel;
import com.herowar.game.models.TowerRestriction;
import com.herowar.game.network.Connection;
import com.herowar.game.network.server.ChatMessagePacket;
import com.herowar.game.network.server.GlobalMessagePacket;
import com.herowar.game.network.server.PlayerStatsUpdatePacket;
import com.herowar.game.network.server.TowerBuildPacket;
import com.herowar.game.network.server.TowerBuildRejectedPacket;
import com.herowar.game.network.server.ChatMessagePacket.Layout;
import com.herowar.game.processor.CacheConstants;
import com.herowar.models.entity.game.Tower;
import com.herowar.models.entity.game.Wave;
import com.herowar.models.entity.game.Waypoint;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

public class ClientTowerRequestHandler {
	private static final Logger.ALogger log = Logger.of(ClientTowerRequestHandler.class);
	private static final DateFormat df = new SimpleDateFormat("hh:mm");

	@Observer(topic = "ClientTowerRequestPacket", referenceStrength = ReferenceStrength.STRONG)
	public static void observe(final Connection connection, final ClientTowerRequestPacket packet) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				double currentGold = 0;
				ConcurrentHashMap<String, Object> playerCache = connection.game().getPlayerCache().get(connection.id());
				if (playerCache.containsKey(CacheConstants.GOLD)) {
					currentGold = (double) playerCache.get(CacheConstants.GOLD);
				}
				Tower entity = JPA.em().find(Tower.class, packet.getId());
				if (entity == null) {
					return;
				}
				com.ardor3d.math.Vector3 position = new com.ardor3d.math.Vector3(packet.getPosition().getX(), 0, packet.getPosition().getZ());
				if (!hasEnoughGold(connection, currentGold, entity.getPrice()) || !isPlaceAllowed(connection, position)) {
					connection.send(new TowerBuildRejectedPacket());
					return;
				}
				TowerModel tower = new TowerModel(connection.game().getNextObjectId(), entity);
				tower.setTranslation(position);
				tower.updateWorldTransform(false);
				tower.setConnection(connection);
				connection.game().getTowerCache().put(tower.getId(), tower);
				connection.game().broadcast(new TowerBuildPacket(tower, packet.getPosition()));
				String message = connection.user().getUsername() + " build " + tower.getName();
				connection.game().broadcast(new GlobalMessagePacket(message));
				connection.game().broadcast(new ChatMessagePacket(Layout.SYSTEM, "[" + df.format(new Date()) + "] System: " + message));
				synchronized (playerCache) {
					playerCache.replace(CacheConstants.GOLD, currentGold - entity.getPrice());
					playerCache.replace(CacheConstants.GOLD_SYNC, (new Date().getTime()));
				}
				connection.send(new PlayerStatsUpdatePacket(null, null, Math.round(currentGold - entity.getPrice()), null, null, entity.getPrice()
						* -1));
			}
		});
	}

	private static boolean hasEnoughGold(Connection connection, double currentGold, int price) {
		if (currentGold < price) {
			connection.send(new GlobalMessagePacket("Insufficient gold to build the tower"));
			return false;
		}
		return true;
	}

	private static boolean isPlaceAllowed(Connection connection, com.ardor3d.math.Vector3 position) {
		if (!checkRestrictions(connection, position)) {
			return false;
		}
		if (!checkWaypoints(connection, position)) {
			return false;
		}
		if (!checkTowers(connection, position)) {
			return false;
		}
		return true;
	}

	private static boolean checkRestrictions(Connection connection, com.ardor3d.math.Vector3 position) {
		Iterator<TowerRestriction> iter = connection.game().getTowerRestrictions().iterator();
		while (iter.hasNext()) {
			TowerRestriction restriction = iter.next();
			if (position.distance(restriction.getPosition().getX(), restriction.getPosition().getY(), restriction.getPosition().getZ()) >= restriction
					.getRadius()) {
				connection.send(new GlobalMessagePacket("Tower must be build in marked area"));
				log.info("Tower build request denied - Distance to marked area is "
						+ position.distance(restriction.getPosition().getX(), restriction.getPosition().getY(), restriction.getPosition().getZ()));
				return false;
			}
		}
		return true;
	}

	private static boolean checkWaypoints(Connection connection, com.ardor3d.math.Vector3 position) {
		Iterator<Wave> iter = connection.game().getMap().getWaves().iterator();
		while (iter.hasNext()) {
			Wave wave = iter.next();
			Iterator<Waypoint> iter2 = wave.getPath().getDbWaypoints().iterator();
			while (iter2.hasNext()) {
				Waypoint waypoint = iter2.next();
				if (waypoint.getPosition().getArdorVector().distance(position) < 50) {
					connection.send(new GlobalMessagePacket("Tower cant be build next to enemy paths"));
					log.info("Tower build request denied - Distance to waypoint " + waypoint.toString() + " is "
							+ waypoint.getPosition().getArdorVector().distance(position));
					return false;
				}
			}
		}
		return true;
	}

	private static boolean checkTowers(Connection connection, com.ardor3d.math.Vector3 position) {
		Iterator<TowerModel> iter = connection.game().getTowerCache().values().iterator();
		while (iter.hasNext()) {
			TowerModel towerModel = iter.next();
			if (towerModel.getTranslation().distance(position) < 25) {
				connection.send(new GlobalMessagePacket("Tower cant be build next to other towers"));
				log.info("Tower build request denied - Distance to tower " + towerModel.toString() + " is "
						+ towerModel.getTranslation().distance(position));
				return false;
			}
		}
		return true;
	}

}
