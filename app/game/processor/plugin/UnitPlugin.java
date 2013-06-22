package game.processor.plugin;

import game.GameSession;
import game.models.UnitModel;
import game.network.server.ObjectOutPacket;
import game.network.server.PlayerLivesUpdatePacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.network.server.UnitOutPacket;
import game.processor.CacheConstants;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
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
 * @author Sebastian Sachtleben
 */
public class UnitPlugin extends AbstractPlugin implements IPlugin {
  private final static Logger.ALogger log = Logger.of(UnitPlugin.class);

  public UnitPlugin(GameProcessor processor) {
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
    UnitOutPacket packet = new UnitOutPacket(unit);
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
      ConcurrentHashMap<String, Object> playerCache = session.getGame().getPlayerCache().get(session.getPlayer().getId());
      long newScore = 0L;
      double newGold = 0L;
      synchronized (playerCache) {
        newScore = ((long) playerCache.get(CacheConstants.SCORE)) + unit.getEntity().getRewardScore();
        playerCache.replace(CacheConstants.SCORE, newScore);
        newGold = ((double) playerCache.get(CacheConstants.GOLD)) + unit.getEntity().getRewardGold();
        playerCache.replace(CacheConstants.KILLS, Long.parseLong(playerCache.get(CacheConstants.KILLS).toString()) + 1L);
        playerCache.replace(CacheConstants.GOLD, newGold);
        playerCache.replace(CacheConstants.GOLD_SYNC, (new Date().getTime()));
      }
      session.getConnection().send(
          Json.toJson(
              new PlayerStatsUpdatePacket(newScore, null, Math.round(newGold), unit.getEntity().getRewardScore(), null, unit.getEntity().getRewardGold()))
              .toString());
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

  @Override
  public State onState() {
    return State.GAME;
  }
  
  @Override
  public String toString() {
    return "UnitPlugin";
  }
}
