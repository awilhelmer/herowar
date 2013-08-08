package jobs;

import java.io.File;

import models.entity.game.Unit;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

/**
 * Syncronizes javascript files from "public/geometries/units" with {@link Unit} models.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true, active = false)
public class UnitImporter extends EntityImporter<Unit> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "units";
	}

}
