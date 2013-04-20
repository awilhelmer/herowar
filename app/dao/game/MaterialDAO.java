package dao.game;

import play.db.jpa.Transactional;
import models.entity.game.Material;
import dao.BaseDAO;

public class MaterialDAO extends BaseDAO<Long, Material> {

  private MaterialDAO() {
    super(Long.class, Material.class);
  }

  private static final MaterialDAO instance = new MaterialDAO();

  @Transactional
  public static Material getMaterialbyId(Long id) {
    return instance.findUnique(id);
  }

}
