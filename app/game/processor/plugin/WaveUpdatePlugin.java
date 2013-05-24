package game.processor.plugin;

import game.GameSession;
import game.event.GameUnitEvent;
import game.network.server.WaveInitPacket;
import game.network.server.WaveUpdatePacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.Topic;
import game.processor.meta.IPlugin;
import game.processor.meta.UpdateSessionPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.entity.game.Unit;
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

  private double spawnRate;
  private int spawnCurrent;
  private int spawnTotal;
  private Date lastSpawnDate;
  private Date waveStartDate;
  private boolean waveUpdated;

  public WaveUpdatePlugin(GameProcessor processor) {
    super(processor);
    waves = getProcessor().getMap().getWaves();
  }

  @Override
  public void process(Double delta) {
    waveUpdated = checkWaveUpdate();
    createUnit();
    super.process(delta);
    waveUpdated = false;
  }

  @Override
  public void processSession(GameSession session) {
    long playerId = session.getUser().getId();
    if (!hashInitPacket(playerId)) {
      long eta = getWaveEta();
      sendPacket(session, new WaveInitPacket(index, eta, total));
      getInitPacket().replace(playerId, true);
    }
    if (waveUpdated) {
      long eta = getWaveEta();
      sendPacket(session, new WaveUpdatePacket(index, eta));
    }
  }

  @Override
  public void load() {
    super.load();
    current = null;
    next = null;
    total = waves.size();
    loadNextWave();
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
    if (next != null && getWaveEta() <= now.getTime()) {
      loadNextWave();
      return true;
    }
    return false;
  }

  private void loadNextWave() {
    current = next;
    next = getNextWave();
    if (current != null) {
      index++;
    } else {
      index = 0;
    }
    spawnRate = current != null ? current.getWaveTime().doubleValue() * 1000 / current.getQuantity().doubleValue() : 0;
    spawnCurrent = 0;
    lastSpawnDate = new Date();
    waveStartDate = new Date();
    log.debug("Load wave " + index + " / " + total + ": " + (current != null ? current.getName() : ""));
  }

  private Wave getNextWave() {
    List<Wave> sortedWaves = new ArrayList<Wave>(waves);
    if (sortedWaves.size() > 0) {
      Collections.sort(sortedWaves, new WaveComparator());
      Wave wave = sortedWaves.get(0);
      log.debug("Next wave: " + wave.getName());
      waves.remove(wave);
      return wave;
    }
    return null;
  }

  private long getWaveEta() {
    long startTime = waveStartDate.getTime();
    if (current != null) {
      startTime += current.getWaveTime() * 1000;
    }
    return next != null ? startTime + (next.getPrepareTime() * 1000) : 0;
  }

  private void createUnit() {
    if (current != null && spawnRate > 0 && spawnCurrent < current.getQuantity()) {
      Date now = new Date();
      if (lastSpawnDate.getTime() + spawnRate <= now.getTime()) {
        Iterator<Unit> iter2 = current.getUnits().iterator();
        Unit unit = iter2.next();
        log.debug("Create enemy for " + current.getName());
        getProcessor().publish(Topic.UNIT, new GameUnitEvent(current.getPath(), unit));
        lastSpawnDate = now;
        spawnCurrent++;
        spawnTotal++;
      }
    }
  }

  @Override
  public String toString() {
    return "WaveUpdatePlugin";
  }
  
  public class WaveComparator implements Comparator<Wave> {
    @Override
    public int compare(Wave w1, Wave w2) {
      return w1.getSortOder().compareTo(w2.getSortOder());
    }
  }
}
