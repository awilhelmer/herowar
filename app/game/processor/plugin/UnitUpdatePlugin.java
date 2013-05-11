package game.processor.plugin;

import game.GameSession;
import game.event.GameUnitEvent;
import game.models.UnitModel;
import game.network.server.ObjectInPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.Topic;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Set;

import models.entity.game.Vector3;
import models.entity.game.Waypoint;

import org.bushe.swing.event.annotation.RuntimeTopicEventSubscriber;

import play.Logger;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Matrix4;
import com.ardor3d.math.Quaternion;
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
    for (UnitModel unit : units) {
      processWaypoints(unit);
      processMoving(unit, delta);
    }
  }

  private void processMoving(UnitModel unit, Double delta) {
    if (!unit.isEndPointReached() && unit.getActiveWaypoint() != null) {

      rotateTo(unit.getActiveWaypoint().getPosition(), unit);
      unit.addTranslation(0, 0, delta * 20); // TODO Speed of unit
    } else {
      // TODO enemy reached his goal ...
    }
  }

  private void rotateTo(Vector3 position, UnitModel unit) {
    com.ardor3d.math.Vector3 target = position.getArdorVector().clone();
    target.setY(0D);
    Matrix3 m = new Matrix3();
    MathUtils.matrixLookAt(target, unit.getTranslation(), new com.ardor3d.math.Vector3(0, 1, 0), m);
    Quaternion qEnd = new Quaternion();
    qEnd.fromRotationMatrix(m);
    Quaternion qStart = new Quaternion();
    qStart.fromRotationMatrix(unit.getRotation());
    unit.setRotation(qStart.slerp(qEnd, 0.07, null));
  }

  private void processWaypoints(UnitModel unit) {
    if (!unit.isEndPointReached()) {
      Waypoint waypoint = unit.getActiveWaypoint();
      ReadOnlyVector3 position = unit.getTranslation();
      if (waypoint != null) {
        log.info(String.format("Diff Pos x=%s y=%s", waypoint.getPosition().getX() - position.getX(), waypoint.getPosition().getZ() - position.getZ()));
        if (Math.abs(waypoint.getPosition().getX() - position.getX()) < 1 && Math.abs(waypoint.getPosition().getZ() - position.getZ()) < 1) {
          int index = unit.getActivePath().getWaypoints().indexOf(waypoint);
          if (index > -1 && index + 1 < unit.getActivePath().getWaypoints().size()) {
            log.info("Unit " + unit.getId() + " reached next waypoint!");
            unit.setActiveWaypoint(unit.getActivePath().getWaypoints().get(index + 1));
          } else {
            unit.setEndPointReached(true);
            unit.setActiveWaypoint(null);
            log.info("Unit " + unit.getId() + " reached endwaypoint!");
          }
        }
      }
    }
  }

  @RuntimeTopicEventSubscriber(methodName = "getUnitTopic")
  public void createUnit(String topic, GameUnitEvent event) {
    Long id = getProcessor().getObjectIdGenerator();
    UnitModel model = new UnitModel(id, event.getUnit().getId());
    model.setActivePath(event.getPath());
    if (event.getPath().getWaypoints() == null) {
      PathDAO.mapWaypoints(event.getPath());
    }
    if (!event.getPath().getWaypoints().isEmpty()) {
      Waypoint waypoint = event.getPath().getWaypoints().get(0);
      model.setActiveWaypoint(waypoint);
      model.setTranslation(waypoint.getPosition().getArdorVector());
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
