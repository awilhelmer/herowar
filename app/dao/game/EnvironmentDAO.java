package dao.game;

import java.util.ArrayList;
import java.util.HashSet;
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
  public static Environment getRoot() {
    List<Object[]> resultSet = JPA.em().createQuery(getRootSelect("Root")).setParameter(0, "Root").getResultList();
    Environment root = new Environment();
    root.setChildren(new HashSet<Environment>());
    for (Object[] row : resultSet) {
      if (root.getId() == null) {
        Long rootId = (Long) row[0];
        root.setId(rootId);
        root.setName((String) row[1]);
      }
      if (row[2] != null) {
        Environment child = new Environment();
        child.setId((Long) row[2]);
        child.setName((String) row[3]);
        root.getChildren().add(child);
        if (row[4] != null) {
          Environment child2 = new Environment();
          child2.setId((Long) row[4]);
          child2.setName((String) row[5]);
          child.getChildren().add(child2);
          addChilds(child2);
        }
      }
    }

    return root;
  }

  @SuppressWarnings("unchecked")
  private static void addChilds(Environment child) {
    List<Object[]> resultSet = JPA.em().createQuery(getRootSelect(null)).setParameter(0, child.getId()).getResultList();
    for (Object[] row : resultSet) {
      if (row[2] != null) {
        Environment child2 = new Environment();
        child2.setId((Long) row[2]);
        child2.setName((String) row[3]);
        child.getChildren().add(child2);
        if (row[4] != null) {
          Environment child3 = new Environment();
          child3.setId((Long) row[4]);
          child3.setName((String) row[5]);
          child2.getChildren().add(child3);
          addChilds(child3);
        }
      }
    }
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

  private static String getRootSelect(String name) {
    StringBuffer SQL = new StringBuffer();
    SQL.append("SELECT e.id, e.name, c.id, c.name, c2.id, c2.name  ");
    SQL.append("FROM Environment e ");
    SQL.append("LEFT JOIN e.geometry g ");
    SQL.append("LEFT JOIN e.children c ");
    SQL.append("LEFT JOIN c.geometry g1 ");
    SQL.append("LEFT JOIN c.children c2 ");
    SQL.append("LEFT JOIN c2.geometry g2 ");
    SQL.append("WHERE ");
    if (name != null) {
      SQL.append("e.name = ?0 ");
    } else {
      SQL.append("e.id = ?0 ");
    }
    SQL.append("AND g1.id is null  ");
    SQL.append("AND g2.id is null  ");

    return SQL.toString();
  }
}
