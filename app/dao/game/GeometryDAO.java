package dao.game;

import models.entity.game.Geometry;
import dao.BaseDAO;

/**
 * @author Sebastian Sachtleben
 */
public class GeometryDAO extends BaseDAO<Long, Geometry> {

  private GeometryDAO() {
    super(Long.class, Geometry.class);
  }

  private static final GeometryDAO instance = new GeometryDAO();

  public static GeometryDAO getInstance() {
    return instance;
  }
}
