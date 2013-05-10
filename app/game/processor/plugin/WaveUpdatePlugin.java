package game.processor.plugin;

import game.GameSession;
import game.network.server.WaveInitPacket;
import game.processor.GameProcessor;
import game.processor.meta.IPlugin;
import game.processor.meta.UpdateSessionPlugin;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import models.entity.game.Wave;
import play.Logger;

/**
 * The WaveUpdatePlugin controls the wave behaviors and update all units.
 * 
 * @author Sebastian Sachtleben
 */
public class WaveUpdatePlugin extends UpdateSessionPlugin implements IPlugin {

  private final static Logger.ALogger log = Logger.of(WaveUpdatePlugin.class);

  private Set<Wave> waves;
  private Wave current;
  private int currentIndex;
  private Date startDate;

  public WaveUpdatePlugin(GameProcessor processor) {
    super(processor);
    waves = getProcessor().getMap().getWaves();
    currentIndex = 1;
    current = getNextWave();
  }

  @Override
  public void processSession(GameSession session) {
    log.debug("Processing " + this.toString() + " with wave " + currentIndex + " / " + waves.size() + " (" + current.toString() + ")");
    long playerId = session.getUser().getId();
    if (!hashInitPacket(playerId)) {
      long eta = startDate.getTime() + (current.getPrepareTime() * 1000);
      sendPacket(session, new WaveInitPacket(currentIndex, eta, waves.size()));
      getInitPacket().replace(playerId, true);
    }
  }

  @Override
  public void load() {
    super.load();
    startDate = new Date();
  }

  @Override
  public void addPlayer(GameSession player) {
    long playerId = player.getUser().getId();
    getInitPacket().put(playerId, false);
  }

  @Override
  public void removePlayer(GameSession player) {
    // TODO Auto-generated method stub
  }

  private Wave getNextWave() {
    Iterator<Wave> wavesIterator = waves.iterator();
    if (wavesIterator.hasNext()) {
      return wavesIterator.next();
    }
    return null;
  }

  @Override
  public String toString() {
    return "WaveUpdatePlugin";
  }
}
