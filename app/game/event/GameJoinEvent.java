package game.event;

import models.entity.game.MatchToken;

import org.webbitserver.WebSocketConnection;

/**
 * This event will be fired if a player requests to join a game.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class GameJoinEvent {

	private long matchId;
	private MatchToken token;
	private WebSocketConnection connection;

	public GameJoinEvent(long matchId, MatchToken token, WebSocketConnection connection) {
		this.matchId = matchId;
		this.token = token;
		this.connection = connection;
	}

	public GameJoinEvent() {
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public MatchToken getToken() {
		return token;
	}

	public void setToken(MatchToken token) {
		this.token = token;
	}

	public WebSocketConnection getConnection() {
		return connection;
	}

	public void setConnection(WebSocketConnection connection) {
		this.connection = connection;
	}
}
