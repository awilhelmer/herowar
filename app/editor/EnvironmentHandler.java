package editor;

import java.io.File;
import java.io.Serializable;

import play.Logger;
import play.Play;

/**
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
    log.info("Starting syncronize between folder and database");
    File baseFolder = new File(Play.application().path(), ENVIRONMENT_FOLDER_PATH);
    readDirectory(baseFolder);
    log.info("Finish syncronize between folder and database");
  }
  
  public void readDirectory(File folder) {
    for (File file : folder.listFiles()) {
      if (file.isDirectory()) {
        log.info("Found directory: " + file.getAbsolutePath());
        readDirectory(file);
      } else {
        log.info("Found geometry: " + file.getAbsolutePath());
      }
    }
  }
  
}
