package editor;

import java.io.File;
import java.io.Serializable;

import models.entity.game.Environment;

/**
 * The EnvironmentHandler synchronize between our geometries environment folder
 * and our database.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class EnvironmentImporter extends AbstractImporter<Environment> implements Serializable {

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

}
