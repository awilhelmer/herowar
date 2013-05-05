package dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import play.db.jpa.JPA;

public abstract class TreeDAO<K extends Serializable, T extends Object> extends BaseDAO<K, T> {

  public TreeDAO(Class<K> idClass, Class<T> entityClass) {
    super(idClass, entityClass);
  }
  
  @SuppressWarnings("unchecked")
  public T getRootEntity() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
    List<Object[]> resultSet = JPA.em().createQuery(getRootSelect("Root")).setParameter(0, "Root").getResultList();
    T root = entityClass.newInstance();
    if (PropertyUtils.isReadable(root, "children")) {
      PropertyUtils.setProperty(root, "children", new HashSet<T>());
    }
    for (Object[] row : resultSet) {
      Long rootId = null;
      if (PropertyUtils.isReadable(root, "id")) {
        rootId = (Long) PropertyUtils.getProperty(root, "id");
      }
      if (rootId == null) {
        rootId = (Long) row[0];
        if (PropertyUtils.isReadable(root, "id")) {
          PropertyUtils.setProperty(root, "id", rootId);
        }
        if (PropertyUtils.isReadable(root, "name")) {
          PropertyUtils.setProperty(root, "name", (String) row[1]);
        }
      }
      if (row[2] != null) {
        T child = entityClass.newInstance();
        if (PropertyUtils.isReadable(child, "id")) {
          PropertyUtils.setProperty(child, "id", row[2]);
        }
        if (PropertyUtils.isReadable(child, "name")) {
          PropertyUtils.setProperty(child, "name", (String) row[3]);
        }
        if (PropertyUtils.isReadable(root, "children")) {
          HashSet<T> children = (HashSet<T>) PropertyUtils.getProperty(root, "children");
          children.add(child);
          PropertyUtils.setProperty(root, "children", children);
        }
        if (row[4] != null) {
          T child2 = entityClass.newInstance();
          if (PropertyUtils.isReadable(child2, "id")) {
            PropertyUtils.setProperty(child2, "id", row[4]);
          }
          if (PropertyUtils.isReadable(child2, "name")) {
            PropertyUtils.setProperty(child2, "name", (String) row[5]);
          }
          if (PropertyUtils.isReadable(child, "children")) {
            HashSet<T> children = (HashSet<T>) PropertyUtils.getProperty(child, "children");
            children.add(child2);
            PropertyUtils.setProperty(child, "children", children);
          }
          addChilds(child2);
        }
      }
    }

    return root;
  }

  @SuppressWarnings("unchecked")
  private void addChilds(T child) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
    Long id = null;
    if (PropertyUtils.isReadable(child, "id")) {
      id = (Long) PropertyUtils.getProperty(child, "id");
    }
    List<Object[]> resultSet = JPA.em().createQuery(getRootSelect(null)).setParameter(0, id).getResultList();
    for (Object[] row : resultSet) {
      if (row[2] != null) {
        T child2 = entityClass.newInstance();
        if (PropertyUtils.isReadable(child2, "id")) {
          PropertyUtils.setProperty(child2, "id", row[2]);
        }
        if (PropertyUtils.isReadable(child2, "name")) {
          PropertyUtils.setProperty(child2, "name", (String) row[3]);
        }
        if (PropertyUtils.isReadable(child, "children")) {
          HashSet<T> children = (HashSet<T>) PropertyUtils.getProperty(child, "children");
          children.add(child2);
          PropertyUtils.setProperty(child, "children", children);
        }
        if (row[4] != null) {
          T child3 = entityClass.newInstance();
          if (PropertyUtils.isReadable(child3, "id")) {
            PropertyUtils.setProperty(child3, "id", row[4]);
          }
          if (PropertyUtils.isReadable(child3, "name")) {
            PropertyUtils.setProperty(child3, "name", (String) row[5]);
          }
          if (PropertyUtils.isReadable(child2, "children")) {
            HashSet<T> children = (HashSet<T>) PropertyUtils.getProperty(child2, "children");
            children.add(child3);
            PropertyUtils.setProperty(child2, "children", children);
          }
          addChilds(child3);
        }
      }
    }
  }
  
  private String getRootSelect(String name) {
    StringBuffer SQL = new StringBuffer();
    SQL.append("SELECT e.id, e.name, c.id, c.name, c2.id, c2.name  ");
    SQL.append("FROM " + entityClass.getSimpleName() + " e ");
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
