package game.processor.plugin;

import game.GameSession;
import game.network.server.GUIElementUpdatePacket;
import game.network.server.TutorialUpdatePacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The tutorial plugin handles the state for the tutorial map.
 * 
 * @author Sebastian Sachtleben
 */
public class TutorialPlugin extends AbstractPlugin implements IPlugin {

  private List<TutorialStep> steps = createTutorialSteps();

  public TutorialPlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(double delta, long now) {
    if (getProcessor().isTutorialUpdate()) {
      getProcessor().setTutorialUpdate(false);
      updateTutorial();
    }
  }

  @Override
  public void load() {
    super.load();
    getProcessor().setTutorialUpdate(true);
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

  private void updateTutorial() {
    Iterator<TutorialStep> iter = steps.iterator();
    if (iter.hasNext()) {
      TutorialStep currentStep = iter.next();
      if (currentStep.isCompleted()) {
        getProcessor().broadcast(new TutorialUpdatePacket(currentStep.getTexts()));
        currentStep.onChange();
        iter.remove();
      }
    } else {
      getProcessor().setWavesFinished(true);
      getProcessor().setUnitsFinished(true);
    }
  }

  private List<TutorialStep> createTutorialSteps() {
    List<TutorialStep> steps = new ArrayList<TutorialStep>();
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Hello Rekrut and Welcome to Herowar. I will help", "you a bit to understand how to play this game." };
      }

      @Override
      public void onChange() {
        // empty
      }

      @Override
      public boolean isCompleted() {
        return true;
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "First lets checkout the interface and I will", "explain each part." };
      }

      @Override
      public void onChange() {
        // empty
      }

      @Override
      public boolean isCompleted() {
        return true;
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "On the top left cornor you will see your score,", "lives and gold. Its important to have allways", "lives left.", "",
            "It is possible to earn gold for killing creeps", "or on some maps you gain gold over time." };
      }

      @Override
      public void onChange() {
        getProcessor().broadcast(new GUIElementUpdatePacket("stats", true, true));
      }

      @Override
      public boolean isCompleted() {
        return true;
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
      public void onChange() {
        getProcessor().broadcast(new GUIElementUpdatePacket("build", true, true));
      }

      @Override
      public boolean isCompleted() {
        return true;
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Now lets check out the map. The enemies will", "try to pass the map on the grey path.", "",
            "Sometimes there is a wave indicator. So you", "see from which side the enemies will come." };
      }

      @Override
      public void onChange() {
        // empty
      }

      @Override
      public boolean isCompleted() {
        return true;
      }
    });
    steps.add(new TutorialStep() {
      @Override
      public String[] getTexts() {
        return new String[] { "Ok its time to build your first tower. You", "should place it on strategic important points." };
      }

      @Override
      public void onChange() {
        // empty
      }

      @Override
      public boolean isCompleted() {
        return true;
      }
    });
    return steps;
  }

  public abstract class TutorialStep {

    public abstract String[] getTexts();

    public abstract void onChange();

    public abstract boolean isCompleted();

  }
}
