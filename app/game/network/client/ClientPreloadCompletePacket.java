package game.network.client;

import java.util.ArrayList;
import java.util.List;

import org.webbitserver.WebSocketConnection;

import play.Logger;

import game.GameSession;
import game.GamesHandler;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.processor.PlayerProcessor;
import game.processor.ProcessorHandler;
import game.processor.meta.IProcessor;

/**
 * Send from client when preloading is done.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientPreloadCompletePacket extends BasePacket implements InputPacket {

  private static final Logger.ALogger log = Logger.of(ClientPreloadCompletePacket.class);
  
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
    session.getClock().reset();
    List<IProcessor> list = new ArrayList<IProcessor>();
    PlayerProcessor playerProcessor = new PlayerProcessor(session.getToken().getToken(), connection);
    session.setPlayerProcessor(playerProcessor);
    list.add(playerProcessor);
    ProcessorHandler handler = new ProcessorHandler(list);
    handler.start();
    GamesHandler.getInstance().getProcessors().put(session, handler);
  }

  @Override
  public String toString() {
    return "ClientPreloadCompletePacket [type=" + type + "]";
  }
}
