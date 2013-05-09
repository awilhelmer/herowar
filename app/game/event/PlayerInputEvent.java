package game.event;

import org.webbitserver.WebSocketConnection;

/**
 * @author Alexander Wilhelmer
 */

public class PlayerInputEvent {

  private WebSocketConnection connection;
  private Long createdTime;
  private Integer keyCode;
  private Boolean active;

  public PlayerInputEvent(WebSocketConnection connection, Long createdTime) {
    this.connection = connection;
    this.createdTime = createdTime;
  }

  public PlayerInputEvent() {
    // empty
  }

  public WebSocketConnection getConnection() {
    return connection;
  }

  public void setConnection(WebSocketConnection connection) {
    this.connection = connection;
  }

  public Long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Long createdTime) {
    this.createdTime = createdTime;
  }

  public Integer getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(Integer keyCode) {
    this.keyCode = keyCode;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

}
