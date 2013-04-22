package dao.game;

import java.util.ArrayList;

import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.Material;
import play.Logger;
import dao.BaseDAO;

/**
 * @author Sebastian Sachtleben
 */
public class GeometryDAO extends BaseDAO<Long, Geometry> {
  private static final Logger.ALogger log = Logger.of(GeometryDAO.class);

  private GeometryDAO() {
    super(Long.class, Geometry.class);
  }

  private static final GeometryDAO instance = new GeometryDAO();

  public static GeometryDAO getInstance() {
    return instance;
  }

  /**
   * For mapping geometry in Client
   * 
   * @param Geometry
   */
  public static void mapMaterials(Geometry geo) {
    geo.setMatIdMapper(new ArrayList<GeoMatId>());
    for (GeoMaterial geoMat : geo.getGeoMaterials()) {
      Material mat = geoMat.getId().getMaterial();
      GeoMatId id = new GeoMatId();
      id.setMaterialIndex(geoMat.getArrayIndex());
      id.setMaterialId(mat.getId());
      geo.getMatIdMapper().add(id);
    }
  }

  public static void createGeoMaterials(Geometry geometry, java.util.Map<Integer, Material> mapping) {
    for (GeoMatId geoMatId : geometry.getMatIdMapper()) {
      if (mapping.containsKey(geoMatId.getMaterialId())) {
        Material mat = mapping.get(geoMatId.getMaterialId());
        GeoMaterial geoMat = new GeoMaterial();
        geoMat.setId(new GeoMaterial.PK());
        geoMat.getId().setMaterial(mat);
        geoMat.getId().setGeometry(geometry);
        geoMat.setArrayIndex(geoMatId.getMaterialIndex());
        geometry.getGeoMaterials().add(geoMat);
      } else {
        log.error(String.format("Material Index <%s> not found in Map!", geoMatId.getMaterialId()));
      }
    }
  }

}
