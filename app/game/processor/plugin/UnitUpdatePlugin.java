package game.processor.plugin;

import game.GameSession;
import game.models.UnitModel;
import game.network.server.ObjectOutPacket;
import game.network.server.PlayerLivesUpdatePacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Waypoint;
import play.Logger;
import play.libs.Json;

import com.ardor3d.math.type.ReadOnlyVector3;

/**
 * 
 * Handles all units for each game. It simulate the units moving and attacking
 * behavior and will by synchronized with all clients in the active game. Also
 * if Turrets fire and hitting (intersection?) the enemies will handle their
 * health and finally their dead
 * 
 * 
 * 1.Build Unit Object (Ardor3D model)
 * 
 * @author Alexander Wilhelmer
 */
public class UnitUpdatePlugin extends AbstractPlugin implements IPlugin {
  private final static Logger.ALogger log = Logger.of(UnitUpdatePlugin.class);

  private final static String SCORE_VALUE = "score";
  private final static String GOLD_VALUE = "gold";
  private final static String GOLD_SYNC = "gold_sync";

  private final static long KILL_REWARD_SCORE = 200;
  private final static double KILL_REWARD_GOLD = 50;

  public UnitUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(double delta, long now) {
    Set<UnitModel> units = getProcessor().getUnits();
    synchronized (units) {
      Iterator<UnitModel> iter = units.iterator();
      while (iter.hasNext()) {
        UnitModel unit = iter.next();
        if (unit.isDeath()) {
          handleUnitDeath(unit);
          iter.remove();
        } else {
          processWaypoints(unit);
          if (!processMoving(unit, delta)) {
            handleUnitDeath(unit);
            iter.remove();
          }
        }
      }
    }
    if (!getProcessor().isUnitsFinished() && getProcessor().isWavesFinished() && units.size() == 0) {
      getProcessor().setUnitsFinished(true);
      log.debug("Units finished!!!!");
    }
  }

  private void handleUnitDeath(UnitModel unit) {
    ObjectOutPacket packet = new ObjectOutPacket(unit.getId());
    broadcast(packet);
    if (unit.isEndPointReached()) {
      if (getProcessor().getMap().getLives() > 0) {
        getProcessor().getMap().setLives(getProcessor().getMap().getLives() - 1);
        PlayerLivesUpdatePacket packet2 = new PlayerLivesUpdatePacket(getProcessor().getMap().getLives());
        broadcast(packet2);
      }
    }
    if (unit.isDeath()) {
      GameSession session = unit.getLastHitTower().getSession();
      ConcurrentHashMap<String, Object> playerCache = session.getGame().getPlayerCache().get(session.getUser().getId());
      long newScore = 0L;
      double newGold = 0L;
      synchronized (playerCache) {
        newScore = ((long) playerCache.get(SCORE_VALUE)) + KILL_REWARD_SCORE;
        playerCache.replace(SCORE_VALUE, newScore);
        newGold = ((double) playerCache.get(GOLD_VALUE)) + KILL_REWARD_GOLD;
        playerCache.replace(GOLD_VALUE, newGold);
        playerCache.replace(GOLD_SYNC, new Date());
      }
      session.getConnection().send(
          Json.toJson(new PlayerStatsUpdatePacket(newScore, null, Math.round(newGold), KILL_REWARD_SCORE, null, Math.round(KILL_REWARD_GOLD))).toString());
    }
  }

  private boolean processMoving(UnitModel unit, Double delta) {
    if (!unit.isEndPointReached() && unit.getActiveWaypoint() != null) {
      unit.rotateTo(delta);
      unit.move(delta, 2);
      unit.updateWorldTransform(false);
      return true;
    }
    return false;
  }

  private void processWaypoints(UnitModel unit) {
    if (!unit.isEndPointReached()) {
      Waypoint waypoint = unit.getActiveWaypoint();
      ReadOnlyVector3 position = unit.getTranslation();
      if (waypoint != null) {
        com.ardor3d.math.Vector3 vWaypoint = waypoint.getPosition().getArdorVector();
        double distance = position.distance(vWaypoint);
        // log.info(String.format("Distance %s", distance));
        if (distance < 2 || distance > unit.getLastDistance()) {
          // log.info("Unit " + unit.getId() + " reached " +
          // waypoint.getName());
          int index = unit.getActivePath().getWaypoints().indexOf(waypoint);
          if (index > -1 && index + 1 < unit.getActivePath().getWaypoints().size()) {
            unit.setActiveWaypoint(unit.getActivePath().getWaypoints().get(index + 1));
            unit.setLastDistance(Double.MAX_VALUE);
          } else {
            unit.setEndPointReached(true);
            unit.setActiveWaypoint(null);
          }
        } else {
          unit.setLastDistance(distance);
        }
      }
    }
  }

  @Override
  public void addPlayer(GameSession player) {
    // Empty
  }

  @Override
  public void removePlayer(GameSession player) {
    // Empty
  }
}
