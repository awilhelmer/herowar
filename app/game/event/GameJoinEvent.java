package game.event;

import models.entity.game.GameToken;

import org.webbitserver.WebSocketConnection;

/**
 * This event will be fired if a player requests to join a game.
 * 
 * @author Alexander Wilhelmer
 */

public class GameJoinEvent {

  private GameToken gameToken;
  private WebSocketConnection connection;
  private String modelName;

  public GameJoinEvent(GameToken gameToken, WebSocketConnection connection, String modelName) {
    this.gameToken = gameToken;
    this.connection = connection;
    this.modelName = modelName;
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

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

}
