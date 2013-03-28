import play.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * Handles global settings.
 * 
 * @author Sebastian Sachtleben
 */
public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Herowar has stated");
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Herowar shutdown...");
  }

}
