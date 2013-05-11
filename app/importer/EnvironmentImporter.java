package importer;

import java.io.File;
import java.io.Serializable;

import models.entity.game.Environment;
import play.Logger;
import play.Logger.ALogger;

/**
 * The EnvironmentHandler synchronize between our geometries environment folder
 * and our database.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class EnvironmentImporter extends TreeImporter<Environment> implements Serializable {

  private static final Logger.ALogger log = Logger.of(EnvironmentImporter.class);
  private static EnvironmentImporter instance;

  public static EnvironmentImporter getInstance() {
    if (instance == null) {
      instance = new EnvironmentImporter();
    }
    return instance;
  }

  @Override
  public String getBaseFolder() {
    return "public" + File.separator + "geometries" + File.separator + "environment";
  }

  @Override
  protected ALogger getLogger() {
    return log;
  }

}
