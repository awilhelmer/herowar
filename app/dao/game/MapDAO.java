package dao.game;

import java.util.ArrayList;

import models.entity.game.Geometry;
import models.entity.game.Map;
import models.entity.game.Material;
import models.entity.game.Mesh;
import play.db.jpa.JPA;
import dao.BaseDAO;

public class MapDAO extends BaseDAO<Long, Map> {

  private MapDAO() {
    super(Long.class, Map.class);
  }

  private static final MapDAO instance = new MapDAO();

  public static Map create(String name, String description, int teamSize) {
    final Map map = new Map();
    map.setName(name);
    map.setDescription(description);
    map.setTeamSize(teamSize);
    return map;
  }

  public static void merge(final Map map, final Map map2) {
    map.setName(map2.getName());
    map.setDescription(map2.getDescription());
    map.setTeamSize(map2.getTeamSize());
    JPA.em().persist(map);
  }

  public static Map getMapByName(String name) {
    return instance.getSingleByPropertyValue("name", name);
  }

  public static Map getMapById(Long id) {
    return instance.findUnique(id);
  }

  /**
   * For loading the MatGeoId.materialId is the real DB id
   * 
   * @param map
   */
  public static void mapMaterials(Map map) {
    Geometry geo = map.getTerrain().getGeometry();
    map.setMaterials(new ArrayList<Material>());
    if (map.getAllMaterials() != null) {
      for (Material mat : map.getAllMaterials()) {
        mat.setMaterialId(mat.getId());
        map.getMaterials().add(mat);
      }
      GeometryDAO.mapMaterials(geo);
    }
  }

  public static void mapStaticGeometries(Map map) {
    map.setStaticGeometries(new ArrayList<Geometry>());
    for (Mesh mesh : map.getObjects()) {
      if (!map.getStaticGeometries().contains(mesh.getGeometry()))
        GeometryDAO.mapMaterials(mesh.getGeometry());
        map.getStaticGeometries().add(mesh.getGeometry());
    }
  }
}
