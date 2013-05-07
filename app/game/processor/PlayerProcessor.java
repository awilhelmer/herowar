package game.processor;

import game.GameSession;
import game.processor.meta.IProcessor;

import org.webbitserver.WebSocketConnection;

import play.Logger;

import com.ardor3d.math.Vector3;

/**
 * Process player input and moves player position at ~58 fps.
 * 
 * @author Alexander Wilhelmer
 */

public class PlayerProcessor extends ConnectionProcessor implements IProcessor {

  private final static Logger.ALogger log = Logger.of(PlayerProcessor.class);
  private long lastTime;
  private Vector3 rotation = new Vector3();
  private double gameTickDuration = 17;

  private boolean speedUp = false;
  private boolean speedDown = false;

  public PlayerProcessor(String topic, WebSocketConnection connection) {
    super(topic, connection);
    // this.addTokenListener(new InputListener());
  }

  @Override
  public void process() {
    update();
  }

  public void update() {
    GameSession session = getSession();
    if (session != null) {
      Long now = System.currentTimeMillis();
      double delta = session.getClock().getDelta();
    }
  }

  @Override
  public int getUpdateTimer() {
    return -1;
  }

  private void updatePlayer(double delta) {
    double realDelta = (delta / (gameTickDuration * 0.001));
    int ticks = (int) realDelta;
    final GameSession session = getSession();
    for (; ticks > 0; ticks--) {
      proccessPlayerSpeed(session, realDelta);
    }
  }

  private void proccessPlayerSpeed(GameSession session, double delta) {

  }

  public Vector3 getRotation() {
    return rotation;
  }

  public void setRotation(Vector3 rotation) {
    this.rotation = rotation;
  }

  @Override
  public void stop() {
    super.stop();
  }
}
