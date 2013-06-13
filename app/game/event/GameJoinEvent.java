package game.event;

import models.entity.game.Match;
import models.entity.game.MatchToken;

import org.webbitserver.WebSocketConnection;

/**
 * This event will be fired if a player requests to join a game.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class GameJoinEvent {

  private Match match;
  private MatchToken token;
  private WebSocketConnection connection;

  public GameJoinEvent(Match match, MatchToken token, WebSocketConnection connection) {
    this.match = match;
    this.token = token;
    this.connection = connection;
  }

  public GameJoinEvent() {
  }

  public Match getMatch() {
    return match;
  }

  public void setMatch(Match match) {
    this.match = match;
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
