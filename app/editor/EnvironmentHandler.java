package editor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import models.entity.game.Environment;
import models.entity.game.Geometry;
import models.entity.game.Material;

import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import dao.game.EnvironmentDAO;
import dao.game.GeometryDAO;
import dao.game.MaterialDAO;

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

  private static ObjectMapper mapper = new ObjectMapper();

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

  private void readDirectory(File folder, Environment parent) {
    for (File file : folder.listFiles()) {
      Environment child = null;
      if (file.isDirectory()) {
        log.info("Found directory: " + file.getAbsolutePath());
        child = createEnvironment(file, parent);
        readDirectory(file, child);
      } else {
        log.info("Found geometry: " + file.getAbsolutePath());
        child = createEnvironment(file, parent);
        child.setGeometry(parseGeometryFile(file));
      }
      parent.getChildren().add(child);
    }
  }

  private Environment createEnvironment(File file, Environment parent) {
    return createEnvironment(WordUtils.capitalize(file.getName().replace(".js", "")), parent);
  }

  private Environment createEnvironment(String name, Environment parent) {
    Environment environment = new Environment(name);
    if (parent != null) {
      environment.setParent(parent);
    }
    return environment;
  }

  private Geometry parseGeometryFile(File file) {
    try {
      Geometry geo = mapper.readValue(file, Geometry.class);
      for (Material mat : geo.getMaterials()) {
        mat.setName(mat.getDbgName());
      }
      
      Map<Integer, Material> matMap = MaterialDAO.mapAndSave(geo.getMaterials());
      GeometryDAO.createGeoMaterials(geo, matMap);
      return geo;
    } catch (IOException e) {
      log.error("Failed to parse geometry file:", e);
    }
    return null;
  }

}
