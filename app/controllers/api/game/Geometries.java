package controllers.api.game;

import static play.libs.Json.toJson;

import java.util.ArrayList;

import models.entity.game.Environment;
import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.Material;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.api.BaseAPI;
import dao.game.EnvironmentDAO;
import dao.game.GeometryDAO;

/**
 * The Geometries controller handle api requests for the Geometry model.
 * 
 * @author Sebastian Sachtleben
 */
public class Geometries extends BaseAPI<Long, Geometry> {

  private Geometries() {
    super(Long.class, Geometry.class);
  }

  public static final Geometries instance = new Geometries();

  @Transactional
  public static Result list() {
    return instance.listAll();
  }

  @Transactional
  public static Result show(Long id) {
    Geometry geo = GeometryDAO.getInstance().findUnique(id);
    GeometryDAO.mapMaterials(geo);
    return ok(toJson(geo));
  }

  @Transactional
  public static Result showByEnv(Long id) {
    Environment env = EnvironmentDAO.getInstance().getById(id);
    if (env == null || env.getGeometry() == null) {
      return badRequest("No result found");
    }
    Geometry geo = env.getGeometry();
    GeometryDAO.mapMaterials(geo); // For global binding ... TODO
    geo.setMaterials(new ArrayList<Material>());
    for (GeoMaterial geoMap : geo.getGeoMaterials()) {
      geo.getMaterials().add(geoMap.getId().getMaterial());
    }

    return ok(toJson(geo));
  }

  @Transactional
  public static Result update(Long id) {
    Geometry geometry = instance.findUnique(id);
    geometry = JPA.em().merge(geometry);
    return ok(toJson(geometry));
  }

  @Transactional
  public static Result delete(Long id) {
    return instance.deleteEntry(id);
  }

  @Transactional
  public static Result add() {
    return instance.addEntry();
  }
}
