package importer;

import java.io.File;

import models.entity.game.Environment;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

/**
 * The EnvironmentHandler synchronize between our geometries environment folder and our database.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true)
public class EnvironmentImporter extends TreeImporter<Environment> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "environment";
	}

}
