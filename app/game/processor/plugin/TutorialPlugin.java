package game.processor.plugin;

import game.GameSession;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

/**
 * The tutorial plugin handles the state for the tutorial map.
 * 
 * @author Sebastian Sachtleben
 */
public class TutorialPlugin extends AbstractPlugin implements IPlugin {

  private int state = 0;
  
  private String[][] texts = {
    { "Hello Rekrut and Welcome to Herowar. I will help", "you a bit to understand how to play this game." },
    { "First lets checkout the interface and I will", "explain each part." },
    { "On the top left cornor you will see your score,", "lives and gold. Its important to have allways", "lives left.", "", "It is possible to earn gold for killing creeps", "or on some maps you gain gold over time." },
    { "On the bottom left cornor you have access to", "the build menu so you can place new towers on", "the map.", "", "Depending on the mission you can choose", "between different towers. Each tower has its own", "unique abilities. Check the build menu tooltips for", "detailed informations." },
    { "Now lets check out the map. The enemies will", "try to pass the map on the grey path.", "", "Sometimes there is a wave indicator. So you", "see from which side the enemies will come." },
    { "Ok its time to build your first tower. You", "should place it on strategic important points." }
  };
  
  public TutorialPlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(double delta, long now) {
    // TODO Auto-generated method stub
  }

  @Override
  public void addPlayer(GameSession session) {
    // Empty
  }

  @Override
  public void removePlayer(GameSession session) {
    // Empty
  }

  @Override
  public State onState() {
    return State.GAME;
  }
  
  @Override
  public String toString() {
    return "TutorialPlugin";
  }
}
