package game.event;

import org.webbitserver.WebSocketConnection;

/**
 * This event will be fired if a player closes the connection.
 * 
 * @author Alexander Wilhelmer
 */

public class GameLeaveEvent {

  private WebSocketConnection connection;

  public GameLeaveEvent(WebSocketConnection connection) {
    this.connection = connection;
  }

  public GameLeaveEvent() {
    // empty
  }

  public WebSocketConnection getConnection() {
    return connection;
  }

  public void setConnection(WebSocketConnection connection) {
    this.connection = connection;
  }

}
