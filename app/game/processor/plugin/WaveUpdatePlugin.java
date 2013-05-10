package game.processor.plugin;

import game.GameSession;
import game.network.server.WaveInitPacket;
import game.network.server.WaveUpdatePacket;
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
  private Wave next;
  private int index;
  private int total;
  
  private Date startDate;
  private boolean waveUpdated = false;

  public WaveUpdatePlugin(GameProcessor processor) {
    super(processor);
    waves = getProcessor().getMap().getWaves();
  }
  
  @Override
  public void process() {
    waveUpdated = checkWaveUpdate();
    super.process();
    waveUpdated = false;
  }

  @Override
  public void processSession(GameSession session) {
    log.debug("Processing " + this.toString() + " with wave " + index + " / " + total + ": " + (current != null ? current.toString() : ""));
    long playerId = session.getUser().getId();
    if (!hashInitPacket(playerId)) {
      long eta = getEta();
      sendPacket(session, new WaveInitPacket(index, eta, total));
      getInitPacket().replace(playerId, true);
    }
    if (waveUpdated) {
      long eta = getEta();
      sendPacket(session, new WaveUpdatePacket(index, eta));
    }
  }

  @Override
  public void load() {
    super.load();
    startDate = new Date();
    index = 0;
    total = waves.size();
    current = null;
    next = getNextWave();
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
  
  private boolean checkWaveUpdate() {
    Date now = new Date();
    if (next != null && startDate.getTime() + next.getPrepareTime() * 1000 <= now.getTime()) {
      current = next;
      next = getNextWave();
      index++;
      startDate = new Date();
      return true;
    }
    return false;
  }

  private Wave getNextWave() {
    Iterator<Wave> wavesIterator = waves.iterator();
    if (wavesIterator.hasNext()) {
      Wave next = wavesIterator.next();
      wavesIterator.remove();
      return next;
    }
    return null;
  }
  
  private long getEta() {
    return next != null ? startDate.getTime() + (next.getPrepareTime() * 1000) : 0;
  }

  @Override
  public String toString() {
    return "WaveUpdatePlugin";
  }
}
