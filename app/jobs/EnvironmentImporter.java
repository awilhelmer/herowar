package jobs;

import java.io.File;

import models.entity.game.Environment;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

/**
 * Syncronizes javascript files from "public/geometries/environment" with {@link Environment} models.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true, active = true)
public class EnvironmentImporter extends EntityImporter<Environment> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "environment";
	}

}
