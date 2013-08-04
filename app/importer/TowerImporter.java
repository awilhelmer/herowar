package importer;

import java.io.File;

import models.entity.game.Tower;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

@StartJob(async = true)
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
