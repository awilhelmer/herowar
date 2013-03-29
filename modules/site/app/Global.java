import common.models.User;

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

    // Create admin user for dev mode
    if (app.isDev()) {
      User user = new User("admin", "admin");
      user.save();
    }
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Herowar shutdown...");
  }

}
