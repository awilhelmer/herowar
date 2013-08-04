package game;

import game.models.BaseModel;
import game.processor.GameProcessor;

import java.io.Serializable;

import models.entity.game.Match;
import models.entity.game.MatchToken;
import models.entity.game.Player;

import org.webbitserver.WebSocketConnection;

/**
 * @author Alexander Wilhelmer
 */
public class Session implements Serializable {
	public final static long PING_INTERVAL = 2000l;
	private static final long serialVersionUID = 7587205545547734770L;

	private long matchId = -1;
	private long playerId = -1;
	private String username = null;

	private Match match;
	private Player player;
	private Clock clock;
	private MatchToken token;

	private BaseModel<?> model;
	private WebSocketConnection connection;
	private GameProcessor game;

	private boolean preloading = true;
	private long latency;

	public Session(Match match, Player player, MatchToken token, WebSocketConnection connection) {
		this.match = match;
		this.matchId = match.getId();
		this.player = player;
		this.playerId = player.getId();
		this.username = player.getUser().getUsername();
		this.token = token;
		this.connection = connection;
		this.clock = new Clock();
	}

	public long getMatchId() {
		return matchId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getUsername() {
		return username;
	}

	public Match getMatch() {
		return match;
	}

	public Player getPlayer() {
		return player;
	}

	public MatchToken getToken() {
		return token;
	}

	public WebSocketConnection getConnection() {
		return connection;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public boolean isPreloading() {
		return preloading;
	}

	public void setPreloading(boolean preloading) {
		this.preloading = preloading;
	}

	public GameProcessor getGame() {
		return game;
	}

	public void setGame(GameProcessor game) {
		this.game = game;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	public BaseModel<?> getModel() {
		return model;
	}

	public void setModel(BaseModel<?> model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return player != null && player.getUser() != null ? player.getUser().getUsername() : "GameSession";
	}

}