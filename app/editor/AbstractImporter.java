package editor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import models.entity.game.Geometry;
import models.entity.game.Material;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger.ALogger;
import play.Play;
import play.db.jpa.JPA;
import dao.game.GeometryDAO;
import dao.game.MaterialDAO;

/**
 * The AbstractImporter provides basic functionality to import json js files to
 * our database.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AbstractImporter<E extends Serializable> {

  private static ObjectMapper mapper = new ObjectMapper();

  private Class<E> clazz;

  public AbstractImporter() {
    this.clazz = getTypeParameterClass();
  }

  public void sync() {
    getLogger().info("Starting synchronize between folder and database");
    File baseFolder = new File(Play.application().path(), getBaseFolder());
    E root = createEntry("Root", null);
    readDirectory(baseFolder, root);
    JPA.em().persist(root);
    getLogger().info("Finish synchronize between folder and database");
  }

  @SuppressWarnings("unchecked")
  private void readDirectory(File folder, E parent) {
    try {
      for (File file : folder.listFiles()) {
        E child = null;
        if (file.isDirectory()) {
          getLogger().info("Found directory: " + file.getAbsolutePath());
          child = createEntry(file, parent);
          readDirectory(file, child);
        } else {
          getLogger().info("Found geometry: " + file.getAbsolutePath());
          child = createEntry(file, parent);
          if (PropertyUtils.isReadable(child, "geometry")) {
            PropertyUtils.setProperty(child, "geometry", parseGeometryFile(file));
          } else {
            getLogger().warn(String.format("Property geometry not found on Class <%s>", child.getClass()));
          }
        }
        if (PropertyUtils.isReadable(child, "children")) {
          Collection<E> childs = (Collection<E>) PropertyUtils.getProperty(parent, "children");
          childs.add(child);

        } else {
          getLogger().warn(String.format("Property children not found on Class <%s>", child.getClass()));
        }
      }

    } catch (Exception e) {
      getLogger().error("", e);
    }
  }

  private E createEntry(File file, E parent) {
    try {
      return createEntry(WordUtils.capitalize(file.getName().replace(".js", "")), parent);
    } catch (Exception e) {
      getLogger().error("", e);
    }
    return null;
  }

  private E createEntry(String name, E parent) {
    E entry = null;
    try {
      entry = clazz.newInstance();
      if (PropertyUtils.isReadable(entry, "name")) {
        PropertyUtils.setProperty(entry, "name", name);
      } else {
        getLogger().warn(String.format("Property name not found on Class <%s>", entry.getClass()));
      }
      if (parent != null && PropertyUtils.isReadable(entry, "parent")) {
        PropertyUtils.setProperty(entry, "parent", parent);
      } else {
        getLogger().warn(String.format("Property parent not found on Class <%s>", entry.getClass()));
      }
    } catch (Exception e) {
      getLogger().error("", e);
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
      getLogger().error("Failed to parse geometry file:", e);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private Class<E> getTypeParameterClass() {
    Type type = getClass().getGenericSuperclass();
    ParameterizedType paramType = (ParameterizedType) type;
    return (Class<E>) paramType.getActualTypeArguments()[0];
  }

  public abstract String getBaseFolder();

  protected abstract ALogger getLogger();
}
