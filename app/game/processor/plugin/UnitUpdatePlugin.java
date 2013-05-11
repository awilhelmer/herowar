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

import org.bushe.swing.event.annotation.RuntimeTopicEventSubscriber;

import com.google.common.eventbus.Subscribe;

import models.entity.game.Path;
import models.entity.game.Unit;
import play.Logger;

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
  public void process() {
    Set<UnitModel> units = getProcessor().getUnits();
    // TODO Process units
  }

  @Override
  public void addPlayer(GameSession player) {

  }

  @Override
  public void removePlayer(GameSession player) {

  }

  @RuntimeTopicEventSubscriber(methodName = "getUnitTopic")
  public void createUnit(String topic, GameUnitEvent event) {
    Long id = getProcessor().getObjectIdGenerator();
    UnitModel model = new UnitModel(id, event.getUnit().getId());
    model.setActivePath(event.getPath());

    getProcessor().getUnits().add(model);
    log.info(String.format("Sending new Unit to all Clients: Uitname %s PathId %s", event.getUnit().getName(), event.getPath().getId()));
    ObjectInPacket packet = new ObjectInPacket(id, event.getUnit().getName(), event.getPath().getId());
    broadcast(packet);

  }

  public String getUnitTopic() {
    return getProcessor().getTopicName(Topic.UNIT);
  }

}
