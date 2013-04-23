package dao.game;

import java.util.ArrayList;
import java.util.List;

import models.entity.game.Environment;
import play.db.jpa.JPA;
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

  private String treeSQL;

  public static EnvironmentDAO getInstance() {
    return instance;
  }

  public static long getEnvironmentCount() {
    return instance.getBaseCount();
  }

  public static List<Environment> getEnvWithGeometries(Long id) {
    List<Environment> result = new ArrayList<Environment>();
    getChildGeometries(result, id);
    return result;

  }

  @SuppressWarnings("unchecked")
  private static void getChildGeometries(List<Environment> set, Long startId) {
    List<Object[]> resultSet = JPA.em().createQuery(getTreeSelect()).setParameter(0, startId).getResultList();
    for (Object[] row : resultSet) {
      // Adding all results ...
      for (int i = 2; i < 9; i += 3) {
        Environment env = getEnvFromRow(row, i);
        if (env != null) {
          set.add(env);
        }
      }
      // Recursive get next depth
      if (row[6] != null) {
        getChildGeometries(set, (Long) row[6]);
      }
    }
  }

  private static Environment getEnvFromRow(Object[] row, int geoIndex) {
    if (geoIndex > 1 && row[geoIndex] != null) {
      Environment env = new Environment();
      env.setId((Long) row[geoIndex - 2]);
      env.setName((String) row[geoIndex - 1]);
      return env;
    }
    return null;
  }

  private static String getTreeSelect() {
    if (instance.treeSQL == null) {
      StringBuffer SQL = new StringBuffer();
      SQL.append("SELECT e.id, e.name, g.id, c.id, c.name, g1.id, c2.id, c2.name, g2.id ");
      SQL.append("FROM Environment e ");
      SQL.append("LEFT JOIN e.geometry g ");
      SQL.append("LEFT JOIN e.children c ");
      SQL.append("LEFT JOIN c.geometry g1 ");
      SQL.append("LEFT JOIN c.children c2 ");
      SQL.append("LEFT JOIN c2.geometry g2 ");
      SQL.append("WHERE ");
      SQL.append("e.id = ?0 ");
      instance.treeSQL = SQL.toString();
    }
    return instance.treeSQL;
  }
}
