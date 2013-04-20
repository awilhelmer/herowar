package dao.game;

import models.entity.game.Object3D;
import play.db.jpa.Transactional;
import dao.BaseDAO;

public class ObjectDAO extends BaseDAO<Long, Object3D> {

  private ObjectDAO() {
    super(Long.class, Object3D.class);
  }

  private static final ObjectDAO instance = new ObjectDAO();

  @Transactional
  public static void merge(Object3D object, Object3D object2) {
    object = instance.merge(object);
    object.setName(object2.getName());
    object.setDescription(object2.getDescription());

  }

}
