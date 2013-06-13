package game.network.client;

import java.util.Iterator;

import game.GameSession;
import game.GamesHandler;
import game.event.PreloadUpdateEvent;
import game.models.TowerModel;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.GameStartPacket;
import game.network.server.TowerBuildPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.Topic;

import models.entity.game.Vector3;

import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.libs.Json;

/**
 * Send from client when preloading is updated. The progress represents the
 * percent of loading and could be a integer value from 0 to 100.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientPreloadUpdatePacket extends BasePacket implements InputPacket {
  private static final Logger.ALogger log = Logger.of(ClientPreloadUpdatePacket.class);

  private Integer progress;

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    log.info("Process " + this.toString());
    // TODO: we should wait here untill all users finished preloading ...
    GameSession session = GamesHandler.getInstance().getConnections().get(connection);
    if (session == null) {
      // TODO: disconnect user here ...
      log.error("GameSession should not be null");
      return;
    }
    if (GameProcessor.State.PRELOAD.equals(session.getGame().getState())) {
      if (progress == 100) {
        log.info("Send preload complete event to " + session.getGame().getTopicName() + " for " + session.getPlayer().getUser().getUsername());
      }
      session.getGame().publish(Topic.PRELOAD, new PreloadUpdateEvent(session.getPlayerId(), progress));
    } else if (progress == 100) {
      session.getConnection().send(Json.toJson(new GameStartPacket()).toString());
      Iterator<TowerModel> iter = session.getGame().getTowerCache().values().iterator();
      while (iter.hasNext()) {
        TowerModel tower = iter.next();
        Vector3 position = new Vector3();
        position.setX(tower.getTranslation().getX());
        position.setY(tower.getTranslation().getY());
        position.setZ(tower.getTranslation().getZ());
        log.info("Send player info about tower " + tower.getId() + " at " + position.toString());
        session.getConnection().send(Json.toJson(new TowerBuildPacket(tower.getId(), tower.getDbId(), tower.getSession().getPlayerId(), position)).toString());
      }
    }
  }

  public Integer getProgress() {
    return progress;
  }

  public void setProgress(Integer progress) {
    this.progress = progress;
  }

  @Override
  public String toString() {
    return "ClientPreloadUpdatePacket [type=" + type + ", progress=" + progress + "]";
  }
}
