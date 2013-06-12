package game;

import game.models.BaseModel;
import game.processor.GameProcessor;
import game.processor.PlayerProcessor;

import java.io.Serializable;

import models.entity.game.MatchToken;
import models.entity.game.Player;

import org.webbitserver.WebSocketConnection;

/**
 * @author Alexander Wilhelmer
 */
public class GameSession implements Serializable {
  public final static long PING_INTERVAL = 2000l;
  private static final long serialVersionUID = 7587205545547734770L;

  private Player player;
  private GameClock clock;
  private MatchToken token;

  private BaseModel model;
  private WebSocketConnection connection;
  private GameProcessor game;

  private long latency;
  private PlayerProcessor playerProcessor;

  public GameSession(Player player, MatchToken token, WebSocketConnection connection) {
    this.player = player;
    this.token = token;
    this.connection = connection;
    this.clock = new GameClock();
  }

  public long getLatency() {
    return latency;
  }

  public void setLatency(long latency) {
    this.latency = latency;
  }

  public Player getPlayer() {
    return player;
  }

  public void setUser(Player player) {
    this.player = player;
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
    return player != null && player.getUser() != null ? player.getUser().getUsername() : "GameSession";
  }

}
