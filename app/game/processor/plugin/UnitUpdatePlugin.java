package game.processor.plugin;

import game.GameSession;
import game.event.GameUnitEvent;
import game.models.UnitModel;
import game.network.server.ObjectInPacket;
import game.network.server.ObjectOutPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.Topic;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Iterator;
import java.util.Set;

import models.entity.game.Unit;
import models.entity.game.Waypoint;

import org.bushe.swing.event.annotation.RuntimeTopicEventSubscriber;

import play.Logger;

import com.ardor3d.math.type.ReadOnlyVector3;

import dao.game.PathDAO;

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

  public UnitUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(Double delta) {
    Set<UnitModel> units = getProcessor().getUnits();
    Iterator<UnitModel> iter = units.iterator();
    while (iter.hasNext()) {
      UnitModel unit = iter.next();
      if (unit.isDeath()) {
        ObjectOutPacket packet = new ObjectOutPacket(unit.getId());
        broadcast(packet);
        iter.remove();
      } else {
        processWaypoints(unit);
        processMoving(unit, delta);
      }
    }
  }

  private void processMoving(UnitModel unit, Double delta) {
    if (!unit.isEndPointReached() && unit.getActiveWaypoint() != null) {
      unit.rotateTo(delta);
      unit.move(delta * 20, 2);
      unit.updateWorldTransform(false);
    } else {
      // TODO enemy reached his goal ...
    }
  }

  private void processWaypoints(UnitModel unit) {
    if (!unit.isEndPointReached()) {
      Waypoint waypoint = unit.getActiveWaypoint();
      ReadOnlyVector3 position = unit.getTranslation();
      if (waypoint != null) {
        com.ardor3d.math.Vector3 vWaypoint = waypoint.getPosition().getArdorVector();
        double distance = position.distance(vWaypoint);
        //log.info(String.format("Distance %s", distance));
        if (distance < 2) {
          log.info("Unit " + unit.getId() + " reached " + waypoint.getName());
          int index = unit.getActivePath().getWaypoints().indexOf(waypoint);
          if (index > -1 && index + 1 < unit.getActivePath().getWaypoints().size()) {
            unit.setActiveWaypoint(unit.getActivePath().getWaypoints().get(index + 1));
          } else {
            unit.setEndPointReached(true);
            unit.setActiveWaypoint(null);
          }
        }
      }
    }
  }

  @RuntimeTopicEventSubscriber(methodName = "getUnitTopic")
  public void createUnit(String topic, GameUnitEvent event) {
    Long id = getProcessor().getObjectIdGenerator();
    UnitModel model = new UnitModel(id, event.getUnit().getId());
    // Matrix3 m = model.getRotation().clone().applyRotationY(90);
    // model.setRotation(m);
    model.setActivePath(event.getPath());
    if (event.getPath().getWaypoints() == null) {
      PathDAO.mapWaypoints(event.getPath());
    }
    if (!event.getPath().getWaypoints().isEmpty()) {
      Waypoint waypoint = event.getPath().getWaypoints().get(0);
      model.setActiveWaypoint(waypoint);
      com.ardor3d.math.Vector3 position = waypoint.getPosition().getArdorVector().clone();
      position.setY(0d);
      model.setTranslation(position);
      model.updateWorldTransform(false);
    } else {
      log.warn("No Waypoint found!");
    }
    getProcessor().getUnits().add(model);
    log.info(String.format("Sending new Unit to all Clients: Uitname %s PathId %s", event.getUnit().getName(), event.getPath().getId()));
    ObjectInPacket packet = new ObjectInPacket(id, event.getUnit().getName(), event.getPath().getId());
    broadcast(packet);
  }

  public String getUnitTopic() {
    return getProcessor().getTopicName(Topic.UNIT);
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
