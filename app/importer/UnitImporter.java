package importer;

import java.io.File;

import models.entity.game.Unit;
import play.Application;
import play.Logger;
import play.Logger.ALogger;

public class UnitImporter extends TreeImporter<Unit> {
	private static final Logger.ALogger log = Logger.of(UnitImporter.class);

	public UnitImporter(Application app) {
		super(app);
	}

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "units";
	}

	@Override
	protected ALogger getLogger() {
		return log;
	}

	@Override
	protected boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".js");
	}
}
