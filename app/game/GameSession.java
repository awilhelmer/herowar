package game;

import java.io.Serializable;

/**
 * @author Alexander Wilhelmer
 */
public class GameSession implements Serializable {
  public final static long PING_INTERVAL = 2000l;
  private static final long serialVersionUID = 7587205545547734770L;


//  private User user;
//
//  private GameToken token;
//
//  private WebSocketConnection connection;
//
//  private GameProcessor game;
//
//  private long latency;
//
//  private PlayerProcessor playerProcessor;
//
//  public GameSession(User user, GameToken token, WebSocketConnection connection, File model, String modelName) {
//    super(user.getId(), user, model, modelName);
//    this.user = user;
//    this.token = token;
//    this.connection = connection;
//
//  }
//
//  public long getLatency() {
//    return latency;
//  }
//
//  public void setLatency(long latency) {
//    this.latency = latency;
//  }
//
//  public User getUser() {
//    return user;
//  }
//
//  public void setUser(User user) {
//    this.user = user;
//  }
//
//  public GameToken getToken() {
//    return token;
//  }
//
//  public void setToken(GameToken token) {
//    this.token = token;
//  }
//
//  public WebSocketConnection getConnection() {
//    return connection;
//  }
//
//  public void setConnection(WebSocketConnection connection) {
//    this.connection = connection;
//  }
//
//  public GameProcessor getGame() {
//    return game;
//  }
//
//  public void setGame(GameProcessor game) {
//    this.game = game;
//  }
//
//  public PlayerProcessor getPlayerProcessor() {
//    return playerProcessor;
//  }
//
//  public void setPlayerProcessor(PlayerProcessor playerProcessor) {
//    this.playerProcessor = playerProcessor;
//  }
//
//  @Override
//  public String toString() {
//    return user != null ? user.getUsername() : "GameSession";
//  }

}
