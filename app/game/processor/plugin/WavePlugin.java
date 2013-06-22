package game.processor.plugin;

import game.GameSession;
import game.models.UnitModel;
import game.network.server.UnitInPacket;
import game.network.server.WaveInitPacket;
import game.network.server.WaveUpdatePacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.meta.IPlugin;
import game.processor.meta.UpdateSessionPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.entity.game.Path;
import models.entity.game.Unit;
import models.entity.game.Vector3;
import models.entity.game.Wave;
import models.entity.game.Waypoint;
import play.Logger;
import dao.game.PathDAO;

/**
 * The WaveUpdatePlugin controls the wave behaviors and update all units.
 * 
 * @author Sebastian Sachtleben
 */
public class WavePlugin extends UpdateSessionPlugin implements IPlugin {
  private final static Logger.ALogger log = Logger.of(WavePlugin.class);

  private List<WaveSpawner> spawners = new ArrayList<WaveSpawner>();
  private List<Wave> waves;

  private Wave current;
  private Wave next;

  private int index;
  private int total;

  private long waveStartDate = 0;
  private boolean waveUpdated = false;

  public WavePlugin(GameProcessor processor) {
    super(processor);
    sortWaves();
  }

  @Override
  public void process(double delta, long now) {
    waveUpdated = checkWaveUpdate(now);
    checkForUnitSpawn(delta, now);
    super.process(delta, now);
    waveUpdated = false;
    if (!getProcessor().isWavesFinished() && next == null && spawners.size() == 0) {
      getProcessor().setWavesFinished(true);
      log.debug("Waves finished!!!!");
    }
  }

  @Override
  public void processSession(GameSession session, double delta, long now) {
    long playerId = session.getPlayer().getId();
    if (!hashInitPacket(playerId)) {
      long eta = getWaveEta();
      List<Vector3> positions = getNextWavePositions();
      List<String> units = getNextWaveUnits();
      sendPacket(session, new WaveInitPacket(index, waveStartDate, eta, total, positions, units));
      getInitPacket().replace(playerId, true);
    }
    if (waveUpdated) {
      long eta = getWaveEta();
      List<Vector3> positions = getNextWavePositions();
      List<String> units = getNextWaveUnits();
      sendPacket(session, new WaveUpdatePacket(index, waveStartDate, eta, positions, units));
    }
  }

  @Override
  public void load() {
    super.load();
    current = null;
    next = null;
    total = waves.size();
    Date now = new Date();
    loadNextWave(now.getTime());
  }

  @Override
  public void addPlayer(GameSession session) {
    long playerId = session.getPlayer().getId();
    getInitPacket().put(playerId, false);
  }

  @Override
  public void removePlayer(GameSession session) {
    // TODO Auto-generated method stub
  }

  private List<Vector3> getNextWavePositions() {
    List<Vector3> positions = new ArrayList<Vector3>();
    if (next != null && next.isRequestable() && next.getPath().getWaypoints().size() > 0) {
      Iterator<Waypoint> iter = next.getPath().getWaypoints().iterator();
      Waypoint waypoint = iter.next();
      positions.add(waypoint.getPosition());
    }
    return positions;
  }

  private List<String> getNextWaveUnits() {
    List<String> names = new ArrayList<String>();
    if (next != null && next.isRequestable() && next.getPath().getWaypoints().size() > 0) {
      Iterator<Unit> iter = next.getUnits().iterator();
      Unit unit = iter.next();
      names.add(unit.getName() + " x" + next.getQuantity());
    }
    return names;
  }

  private boolean checkWaveUpdate(long now) {
    if (next != null) {
      if ((next.isAutostart() && getWaveEta() <= now) || waveIsRequestable(now)) {
        getProcessor().setWaveRequest(false);
        loadNextWave(now);
        return true;
      }
    }
    return false;
  }

  private boolean waveIsRequestable(long now) {
    // TODO: this is ugly but otherwise multiple waves will be requested with
    // one click...
    if (waveStartDate + 2000 > now && getProcessor().isWaveRequest()) {
      getProcessor().setWaveRequest(false);
      return false;
    }
    return next.isRequestable() && getProcessor().isWaveRequest() && waveStartDate + 2000 <= now;
  }

  private void loadNextWave(long now) {
    current = next;
    next = getNextWave();
    if (next != null && next.getPath() != null && next.getPath().getWaypoints() == null) {
      PathDAO.mapWaypoints(next.getPath());
    }
    if (current != null) {
      index++;
      if (index == 1) {
        getProcessor().setUpdateGold(true);
      }
    } else {
      index = 0;
    }
    log.debug("Load wave " + index + " / " + total + ": " + (current != null ? current.getName() : ""));
    if (current != null) {
      spawners.add(new WaveSpawner(this, current, now));
    }
    waveStartDate = now;
  }

  private Wave getNextWave() {
    if (waves.size() > 0) {
      Wave wave = waves.get(0);
      log.debug("Next wave: " + wave.getName());
      waves.remove(wave);
      return wave;
    }
    return null;
  }

  private long getWaveEta() {
    long startTime = waveStartDate;
    if (current != null) {
      startTime += current.getWaveTime() * 1000;
    }
    return next != null ? startTime + (next.getPrepareTime() * 1000) : startTime;
  }

  private void checkForUnitSpawn(double delta, long now) {
    Iterator<WaveSpawner> iter = spawners.iterator();
    while (iter.hasNext()) {
      WaveSpawner spawner = iter.next();
      spawner.process(delta, now);
      if (spawner.isDone()) {
        iter.remove();
      }
    }
  }

  public void createUnit(Path path, Unit entity) {
    UnitModel model = new UnitModel(getProcessor().getNextObjectId(), entity, path);
    model.updatePositionFromWaypoints();
    synchronized (getProcessor().getUnits()) {
      getProcessor().getUnits().add(model);
    }
    broadcast(new UnitInPacket(model));
  }

  private void sortWaves() {
    waves = new ArrayList<Wave>(getProcessor().getMap().getWaves());
    if (waves.size() > 0) {
      Collections.sort(waves, new WaveComparator());
    }
  }

  @Override
  public State onState() {
    return State.GAME;
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

  public class WaveSpawner {

    private WavePlugin plugin;
    private Wave wave;
    private double spawnRate;
    private int spawnCurrent = 0;
    private long lastSpawnDate;

    public WaveSpawner(WavePlugin plugin, Wave wave, long now) {
      this.plugin = plugin;
      this.wave = wave;
      this.spawnRate = calculateSpawnRate();
      this.lastSpawnDate = now - Math.round(this.spawnRate + 1);
    }

    public void process(double delta, long now) {
      if (wave != null && spawnRate > 0 && spawnCurrent < wave.getQuantity()) {
        if (lastSpawnDate + spawnRate <= now) {
          Iterator<Unit> iter = wave.getUnits().iterator();
          Unit unit = iter.next();
          lastSpawnDate = now;
          spawnCurrent++;
          log.info(String.format("Wave %d/%d - Sending new unit: %s - Path %s - %d/%d", index, total, unit.getName(), wave.getPath().getId(), spawnCurrent,
              wave.getQuantity()));
          plugin.createUnit(wave.getPath(), unit);
        }
      }
    }

    public boolean isDone() {
      return spawnCurrent >= wave.getQuantity();
    }

    private double calculateSpawnRate() {
      return wave != null ? wave.getWaveTime().doubleValue() * 1000 / wave.getQuantity().doubleValue() : 0;
    }
  }
}
