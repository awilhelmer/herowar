package dao.game;

import java.util.HashSet;
import java.util.Set;

import play.Logger;

import models.entity.game.Environment;
import models.entity.game.Geometry;
import dao.BaseDAO;

/**
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class EnvironmentDAO extends BaseDAO<Long, Environment> {

  private EnvironmentDAO() {
    super(Long.class, Environment.class);
  }

  private static final EnvironmentDAO instance = new EnvironmentDAO();

  public static EnvironmentDAO getInstance() {
    return instance;
  }

  public static long getEnvironmentCount() {
    return instance.getBaseCount();
  }
  
  public static Set<Geometry> getGeometries(Long id) {
    Set<Geometry> geometries = new HashSet<Geometry>();
    getChildGeometries(geometries, instance.getById(id));
    return geometries;
  }
  
  private static void getChildGeometries(Set<Geometry> set, Environment env) {
    getChildGeometries(set, env.getChildren());
  }
  
  private static void getChildGeometries(Set<Geometry> set, Set<Environment> env) {
    for (Environment currentEnv : env) {
      if (currentEnv.getGeometry() != null) {
        set.add(currentEnv.getGeometry());
      }
      getChildGeometries(set, currentEnv.getChildren());
    }
  }
}
