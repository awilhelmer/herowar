package game;

import game.models.BaseModel;
import game.processor.GameProcessor;
import game.processor.PlayerProcessor;

import java.io.Serializable;

import models.entity.User;
import models.entity.game.GameToken;

import org.webbitserver.WebSocketConnection;

/**
 * @author Alexander Wilhelmer
 */
public class GameSession implements Serializable {
  public final static long PING_INTERVAL = 2000l;
  private static final long serialVersionUID = 7587205545547734770L;

  private User user;
  private GameClock clock;
  private GameToken token;

  private BaseModel model;
  private WebSocketConnection connection;
  private GameProcessor game;

  private long latency;
  private PlayerProcessor playerProcessor;

  public GameSession(User user, GameToken token, WebSocketConnection connection) {
    // super(user.getId(), user, model, modelName);
    this.user = user;
    this.token = token;
    this.connection = connection;
    this.clock = new GameClock();
    // TODO init model ...
  }

  public long getLatency() {
    return latency;
  }

  public void setLatency(long latency) {
    this.latency = latency;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public GameToken getToken() {
    return token;
  }

  public void setToken(GameToken token) {
    this.token = token;
  }

  public WebSocketConnection getConnection() {
    return connection;
  }

  public void setConnection(WebSocketConnection connection) {
    this.connection = connection;
  }

  public GameProcessor getGame() {
    return game;
  }

  public void setGame(GameProcessor game) {
    this.game = game;
  }

  public PlayerProcessor getPlayerProcessor() {
    return playerProcessor;
  }

  public void setPlayerProcessor(PlayerProcessor playerProcessor) {
    this.playerProcessor = playerProcessor;
  }

  public GameClock getClock() {
    return clock;
  }

  public void setClock(GameClock clock) {
    this.clock = clock;
  }

  public BaseModel getModel() {
    return model;
  }

  public void setModel(BaseModel model) {
    this.model = model;
  }

  @Override
  public String toString() {
    return user != null ? user.getUsername() : "GameSession";
  }

}
