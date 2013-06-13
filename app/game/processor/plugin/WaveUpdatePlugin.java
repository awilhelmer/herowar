package game.processor.plugin;

import game.GameSession;
import game.models.UnitModel;
import game.network.server.ObjectInPacket;
import game.network.server.WaveInitPacket;
import game.network.server.WaveUpdatePacket;
import game.processor.GameProcessor;
import game.processor.meta.IPlugin;
import game.processor.meta.UpdateSessionPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
  public void process(double delta, long now) {
    waveUpdated = checkWaveUpdate();
    checkForUnitSpawn();
    super.process(delta, now);
    waveUpdated = false;
    if (!getProcessor().isWavesFinished() && waves.size() == 0 && getWaveEta() <= now) {
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
      sendPacket(session, new WaveInitPacket(index, eta, total, positions));
      getInitPacket().replace(playerId, true);
    }
    if (waveUpdated) {
      long eta = getWaveEta();
      List<Vector3> positions = getNextWavePositions();
      sendPacket(session, new WaveUpdatePacket(index, eta, positions));
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
    if (next != null && next.isRequestable() && next.getPath().getDbWaypoints().size() > 0) {
      Iterator<Waypoint> iter = next.getPath().getDbWaypoints().iterator();
      Waypoint waypoint = iter.next();
      positions.add(waypoint.getPosition());
    }
    return positions;
  }

  private boolean checkWaveUpdate() {
    Date now = new Date();
    if (next != null) {
      if ((next.isAutostart() && getWaveEta() <= now.getTime()) || (next.isRequestable() && getProcessor().isWaveRequest())) {
        getProcessor().setWaveRequest(false);
        loadNextWave();
        return true;
      }
    }
    return false;
  }

  private void loadNextWave() {
    current = next;
    next = getNextWave();
    if (current != null) {
      index++;
      if (index == 1) {
        getProcessor().setUpdateGold(true);
      }
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
    return next != null ? startTime + (next.getPrepareTime() * 1000) : startTime;
  }

  private void checkForUnitSpawn() {
    if (current != null && spawnRate > 0 && spawnCurrent < current.getQuantity()) {
      Date now = new Date();
      if (lastSpawnDate.getTime() + spawnRate <= now.getTime()) {
        Iterator<Unit> iter2 = current.getUnits().iterator();
        Unit unit = iter2.next();
        log.debug("Create enemy for " + current.getName());
        createUnit(current.getPath(), unit);
        lastSpawnDate = now;
        spawnCurrent++;
        spawnTotal++;
      }
    }
  }

  public void createUnit(Path path, Unit entity) {
    Long id = getProcessor().getNextObjectId();
    UnitModel model = new UnitModel(id, entity.getId(), entity);
    model.setActivePath(path);
    if (path.getWaypoints() == null) {
      PathDAO.mapWaypoints(path);
    }
    if (!path.getWaypoints().isEmpty()) {
      Waypoint waypoint = path.getWaypoints().get(0);
      model.setActiveWaypoint(waypoint);
      com.ardor3d.math.Vector3 position = waypoint.getPosition().getArdorVector().clone();
      position.setY(0d);
      model.setTranslation(position);
      model.updateWorldTransform(false);
    } else {
      log.warn("No Waypoint found!");
    }
    synchronized (getProcessor().getUnits()) {
      getProcessor().getUnits().add(model);
    }
    log.info(String.format("Sending new Unit to all Clients: Uitname %s PathId %s", entity.getName(), path.getId()));
    ObjectInPacket packet = new ObjectInPacket(id, entity, path.getId());
    broadcast(packet);
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
