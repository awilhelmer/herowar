import com.herowar.game.Games;

import play.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * Handles global settings.
 * 
 * @author Sebastian Sachtleben
 */
public class Global extends GlobalSettings {
	private static final Logger.ALogger log = Logger.of(Global.class);

	@Override
	public void onStart(Application app) {
		log.info("Herowar stated");
	}

	@Override
	public void onStop(Application app) {
		log.info("Herowar shutdown...");
		Games.shutdown();
	}
}
