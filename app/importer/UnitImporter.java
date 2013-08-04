package importer;

import java.io.File;

import models.entity.game.Unit;

import com.ssachtleben.play.plugin.cron.annotations.StartJob;

@StartJob(async = true)
public class UnitImporter extends TreeImporter<Unit> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "units";
	}

	@Override
	protected boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".js");
	}
}
