package jobs;

import java.io.File;

import models.entity.game.Tower;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

/**
 * Syncronizes javascript files from "public/geometries/towers" with {@link Tower} models.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true, active = false)
public class TowerImporter extends EntityImporter<Tower> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "towers";
	}

}
