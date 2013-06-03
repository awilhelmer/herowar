package importer;

import java.io.File;

import models.entity.game.Tower;
import play.Logger;
import play.Logger.ALogger;

public class TowerImporter extends FolderImporter<Tower> {
  private static final Logger.ALogger log = Logger.of(TowerImporter.class);
  private static TowerImporter instance;

  public static TowerImporter getInstance() {
    if (instance == null) {
      instance = new TowerImporter();
    }
    return instance;
  }

  @Override
  public String getBaseFolder() {
    return "public" + File.separator + "geometries" + File.separator + "towers";
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
