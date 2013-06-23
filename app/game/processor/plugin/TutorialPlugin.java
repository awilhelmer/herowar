package game.processor.plugin;

import game.GameSession;
import game.network.server.GUIElementUpdatePacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.network.server.TutorialUpdatePacket;
import game.processor.CacheConstants;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Tower;

/**
 * The tutorial plugin handles the state for the tutorial map.
 * 
 * @author Sebastian Sachtleben
 */
public class TutorialPlugin extends AbstractPlugin implements IPlugin {

  private List<TutorialStep> steps = createTutorialSteps();
  private TutorialStep current = null;

  public TutorialPlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(double delta, long now) {
    if (current != null && current.isCompleted(delta, now)) {
      steps.remove(current);
      updateTutorial(delta, now);
    }
  }

  @Override
  public void load() {
    super.load();
    updateTutorial(0d, 0l);
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

  private void updateTutorial(double delta, long now) {
    Iterator<TutorialStep> iter = steps.iterator();
    if (iter.hasNext()) {
      current = iter.next();
      getProcessor().broadcast(new TutorialUpdatePacket(current.getTexts()));
      current.onChange(delta, now);
    } else {
      getProcessor().setWavesFinished(true);
      getProcessor().setUnitsFinished(true);
    }
    getProcessor().setTutorialUpdate(false);
  }

  private List<TutorialStep> createTutorialSteps() {
    List<TutorialStep> steps = new ArrayList<TutorialStep>();
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Hello Rekrut and Welcome to Herowar. I will help", "you a bit to understand how to play this game." };
      }

      @Override
      public void onChange(double delta, long now) {
        // empty
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return getProcessor().isTutorialUpdate();
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "First lets checkout the interface and I will", "explain each part." };
      }

      @Override
      public void onChange(double delta, long now) {
        // empty
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return getProcessor().isTutorialUpdate();
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "On the top left cornor you will see your score,", "lives and gold. Its important to have allways", "lives left.", "",
            "It is possible to earn gold for killing creeps", "or on some maps you gain gold over time." };
      }

      @Override
      public void onChange(double delta, long now) {
        getProcessor().broadcast(new GUIElementUpdatePacket("stats", true, true));
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return getProcessor().isTutorialUpdate();
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "On the bottom left cornor you have access to", "the build menu so you can place new towers on", "the map.", "",
            "Depending on the mission you can choose", "between different towers. Each tower has its own",
            "unique abilities. Check the build menu tooltips for", "detailed informations." };
      }

      @Override
      public void onChange(double delta, long now) {
        getProcessor().broadcast(new GUIElementUpdatePacket("build", true, true));
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return getProcessor().isTutorialUpdate();
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Now lets check out the map. The enemies will", "try to pass the map on the grey path.", "",
            "Sometimes there is a wave indicator. So you", "see from which side the enemies will come." };
      }

      @Override
      public void onChange(double delta, long now) {
        // empty
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return getProcessor().isTutorialUpdate();
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Ok its time to build your first tower. I give you", "some credits. Build a tower on the marked ",
            "area. Its a strategic important point." };
      }

      @Override
      public void onChange(double delta, long now) {
        Tower tower = getProcessor().getMap().getTowers().iterator().next();
        Iterator<ConcurrentHashMap<String, Object>> iter = getProcessor().getPlayerCache().values().iterator();
        while (iter.hasNext()) {
          ConcurrentHashMap<String, Object> entry = iter.next();
          entry.replace(CacheConstants.GOLD, tower.getPrice().doubleValue());
          entry.replace(CacheConstants.GOLD_UPDATE, now);
          entry.replace(CacheConstants.GOLD_SYNC, now);
        }
        getProcessor().broadcast(new PlayerStatsUpdatePacket(null, null, tower.getPrice().longValue(), null, null, tower.getPrice()));
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return !getProcessor().getTowerCache().isEmpty();
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Nice work!" };
      }

      @Override
      public void onChange(double delta, long now) {
        // empty
      }

      @Override
      public boolean isCompleted(double delta, long now) {
        return getProcessor().isTutorialUpdate();
      }
    });
    return steps;
  }

  public abstract class TutorialStep {

    public abstract String[] getTexts();

    public abstract void onChange(double delta, long now);

    public abstract boolean isCompleted(double delta, long now);

  }
}
