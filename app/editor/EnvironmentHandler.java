package editor;

import java.io.File;
import java.io.Serializable;

import models.entity.game.Environment;

import org.apache.commons.lang.WordUtils;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import dao.game.EnvironmentDAO;

/**
 * The EnvironmentHandler synchronize between our geometries environment folder
 * and our database.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class EnvironmentHandler implements Serializable {

  public static final String ENVIRONMENT_FOLDER_PATH = "public" + File.separator + "geometries" + File.separator + "environment";

  private static final Logger.ALogger log = Logger.of(EnvironmentHandler.class);

  private static EnvironmentHandler instance;

  public static EnvironmentHandler getInstance() {
    if (instance == null) {
      instance = new EnvironmentHandler();
    }
    return instance;
  }

  public void sync() {
    if (EnvironmentDAO.getEnvironmentCount() != 0)
      return;
    log.info("Starting synchronize between folder and database");
    File baseFolder = new File(Play.application().path(), ENVIRONMENT_FOLDER_PATH);
    Environment root = createEnvironment("Root", null);
    readDirectory(baseFolder, root);
    JPA.em().persist(root);
    log.info("Finish synchronize between folder and database");
  }

  public void readDirectory(File folder, Environment parent) {
    for (File file : folder.listFiles()) {
      Environment child = null;
      if (file.isDirectory()) {
        log.info("Found directory: " + file.getAbsolutePath());
        child = createEnvironment(file, parent);
        readDirectory(file, child);
      } else {
        log.info("Found geometry: " + file.getAbsolutePath());
        child = createEnvironment(file, parent);
      }
      parent.getChildren().add(child);
    }
  }

  public Environment createEnvironment(File file, Environment parent) {
    return createEnvironment(WordUtils.capitalize(file.getName()), parent);
  }

  public Environment createEnvironment(String name, Environment parent) {
    Environment environment = new Environment(name);
    if (parent != null) {
      environment.setParent(parent);
    }
    return environment;
  }

}
