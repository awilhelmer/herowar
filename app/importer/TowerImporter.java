package importer;

import java.io.File;

import models.entity.game.Tower;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

/**
 * Syncronizes javascript files from "public/geometries/towers" with {@link Tower} models.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true, active = false)
public class TowerImporter extends FolderImporter<Tower> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "towers";
	}

	@Override
	protected boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".js");
	}
}
