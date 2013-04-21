package dao.game;

import models.entity.game.Material;
import dao.BaseDAO;

public class MaterialDAO extends BaseDAO<Long, Material> {

  private MaterialDAO() {
    super(Long.class, Material.class);
  }

  private static final MaterialDAO instance = new MaterialDAO();

  public static Material getMaterialbyId(Long id) {
    return instance.findUnique(id);
  }
  
  public static Material mergeMaterial(Material material) {
    return instance.merge(material);
  }

}
