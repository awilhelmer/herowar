package controllers.api.game;

import static play.libs.Json.toJson;

import java.util.Collections;

import models.entity.game.Environment;
import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.Tower;
import models.entity.game.Unit;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;
import util.MaterialComparator;
import controllers.api.BaseAPI;
import dao.game.EnvironmentDAO;
import dao.game.GeometryDAO;
import dao.game.TowerDAO;
import dao.game.UnitDAO;

/**
 * The Geometries controller handle api requests for the Geometry model.
 * 
 * @author Sebastian Sachtleben
 */
public class Geometries extends BaseAPI<Long, Geometry> {
	private static final Logger.ALogger log = Logger.of(Geometries.class);

	private Geometries() {
		super(Long.class, Geometry.class);
	}

	public static final Geometries instance = new Geometries();

	@Transactional
	public static Result list() {
		log.warn("called listAll without Excludes!");
		return instance.listAll();
	}

	@Transactional
	public static Result show(Long id) {
		Geometry geo = GeometryDAO.getInstance().findUnique(id);
		handleGeo(geo);
		return ok(toJson(geo));
	}

	@Transactional
	public static Result showByEnv(Long id) {
		// response().setHeader(EXPIRES, "Thu, 16 Feb 2023 20:00:00 GMT");
		Environment env = EnvironmentDAO.getInstance().getById(id);
		if (env == null || env.getGeometry() == null) {
			return badRequest("No result found");
		}
		Geometry geo = env.getGeometry();
		handleGeo(geo);
		JsonNode node = toJson(geo);
		return ok(node);
	}

	@Transactional
	public static Result showByUnit(Long id) {
		// response().setHeader(EXPIRES, "Thu, 16 Feb 2023 20:00:00 GMT");
		Unit unit = UnitDAO.getInstance().getById(id);
		if (unit == null || unit.getGeometry() == null) {
			return badRequest("No result found");
		}
		Geometry geo = unit.getGeometry();
		handleGeo(geo);
		JsonNode node = toJson(geo);
		return ok(node);
	}

	@Transactional
	public static Result showByTower(Long id) {
		// response().setHeader(EXPIRES, "Thu, 16 Feb 2023 20:00:00 GMT");
		Tower tower = TowerDAO.getInstance().getById(id);
		if (tower == null || tower.getGeometry() == null) {
			return badRequest("No result found");
		}
		Geometry geo = tower.getGeometry();
		handleGeo(geo);
		JsonNode node = toJson(geo);
		return ok(node);
	}

	private static void handleGeo(Geometry geo) {
		GeometryDAO.mapMaterials(geo); // For global binding ... TODO
		geo.getMaterials().clear();
		for (GeoMaterial geoMap : geo.getGeoMaterials()) {
			geo.getMaterials().add(geoMap.getId().getMaterial());
		}
		Collections.sort(geo.getMaterials(), new MaterialComparator());
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
