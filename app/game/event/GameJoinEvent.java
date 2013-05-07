package game.event;

import models.entity.game.GameToken;

import org.webbitserver.WebSocketConnection;

/**
 * This event will be fired if a player requests to join a game.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */

public class GameJoinEvent {

  private GameToken gameToken;
  private WebSocketConnection connection;

  public GameJoinEvent(GameToken gameToken, WebSocketConnection connection) {
    this.gameToken = gameToken;
    this.connection = connection;
  }

  public GameJoinEvent() {
    // empty
  }

  public GameToken getGameToken() {
    return gameToken;
  }

  public void setGameToken(GameToken gameToken) {
    this.gameToken = gameToken;
  }

  public WebSocketConnection getConnection() {
    return connection;
  }

  public void setConnection(WebSocketConnection connection) {
    this.connection = connection;
  }
}
