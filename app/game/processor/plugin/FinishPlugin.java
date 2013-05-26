package game.processor.plugin;

import java.util.Date;

import game.GameSession;
import game.network.server.GameDefeatPacket;
import game.network.server.GameVictoryPacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

/**
 * The FinishPlugin sends informations about the state of the game and clean up.
 * 
 * @author Sebastian Sachtleben
 */
public class FinishPlugin extends AbstractPlugin implements IPlugin {

  private Date finishTimer;
  private boolean done = false;
  
  public FinishPlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(Double delta) {
    Date now = new Date();
    if (finishTimer == null) {
      finishTimer = now;
    }
    if (!done && finishTimer.getTime() + 2000 <= now.getTime()) {
      done = true;
      finishTimer = now;
      if (getMap().getLives() <= 0) {
        GameDefeatPacket packet = new GameDefeatPacket();
        broadcast(packet);
      } else {
        GameVictoryPacket packet = new GameVictoryPacket();
        broadcast(packet);
      }
    } else if (done && finishTimer.getTime() + 2000 <= now.getTime()) {
      // TODO: stop properly the game here and cleanup
      getProcessor().stop();
    }
  }

  @Override
  public void addPlayer(GameSession player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void removePlayer(GameSession player) {
    // TODO Auto-generated method stub
  }
}
