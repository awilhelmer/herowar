package dao.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

  public static void mapMaterials(Geometry geo, List<Material> materials) {
    geo.setMaterialsIndex(new ArrayList<Integer>());
    for (GeoMaterial geoMat : geo.getGeoMaterials()) {
      Material mat = geoMat.getId().getMaterial();
      int index = materials.indexOf(mat);
      if (index > -1 && index == geoMat.getArrayIndex().intValue()) {
        geo.getMaterialsIndex().add(index);
      } else {
        log.error(String.format("Material Index <%s> and Geometry Index <%s> arent the same!", index, geoMat.getArrayIndex()));
      }

    }
    Collections.sort(geo.getMaterialsIndex()); // sorted indices
  }

  public static void createGeoMaterials(Geometry geometry, java.util.Map<Integer, Material> mapping) {
    for (Integer index : geometry.getMaterialsIndex()) {
      if (mapping.containsKey(index)) {
        Material mat = mapping.get(geometry.getMaterialsIndex());
        GeoMaterial geoMat = new GeoMaterial();
        geoMat.setId(new GeoMaterial.PK());
        geoMat.getId().setMaterial(mat);
        geoMat.getId().setGeometry(geometry);
        geoMat.setArrayIndex(Integer.valueOf(index).longValue());
        geometry.getGeoMaterials().add(geoMat);
      } else {
        log.error(String.format("Material Index <%s> not found in Map!", index));
      }
    }
  }

}
