package editor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;

import models.entity.game.Environment;
import models.entity.game.Geometry;
import models.entity.game.Material;
import dao.game.EnvironmentDAO;
import dao.game.GeometryDAO;
import dao.game.MaterialDAO;

/**
 * The AbstractImporter provides basic functionality to import json js files to
 * our database.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AbstractImporter<E extends Serializable> {

  private static final Logger.ALogger log = Logger.of(AbstractImporter.class);

  private static ObjectMapper mapper = new ObjectMapper();

  private Class<E> clazz;
  
  public AbstractImporter() {
    this.clazz = getTypeParameterClass();
  }

  public void sync() {
    log.info("Starting synchronize between folder and database");
    File baseFolder = new File(Play.application().path(), getBaseFolder());
    E root = createEntry("Root", null);
    readDirectory(baseFolder, root);
    JPA.em().persist(root);
    log.info("Finish synchronize between folder and database");
  }

  private void readDirectory(File folder, E parent) {
    for (File file : folder.listFiles()) {
      E child = null;
      if (file.isDirectory()) {
        log.info("Found directory: " + file.getAbsolutePath());
        child = createEntry(file, parent);
        readDirectory(file, child);
      } else {
        log.info("Found geometry: " + file.getAbsolutePath());
        child = createEntry(file, parent);
        child.setGeometry(parseGeometryFile(file));
      }
      parent.getChildren().add(child);
    }
  }

  private E createEntry(File file, E parent) {
    return createEntry(WordUtils.capitalize(file.getName().replace(".js", "")), parent);
  }

  private E createEntry(String name, E parent) {
    E entry = clazz.newInstance();
    entry.setName(name);
    if (parent != null) {
      entry.setParent(parent);
    }
    return entry;
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
  
  @SuppressWarnings("unchecked")
  private Class<E> getTypeParameterClass()
  {
      Type type = getClass().getGenericSuperclass();
      ParameterizedType paramType = (ParameterizedType) type;
      return (Class<E>) paramType.getActualTypeArguments()[0];
  }
  
  public abstract String getBaseFolder();

}
