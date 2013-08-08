package com.herowar.controllers.api.game;

import static play.libs.Json.toJson;

import java.util.Collections;


import org.codehaus.jackson.JsonNode;

import com.herowar.controllers.api.BaseAPI;
import com.herowar.dao.game.EnvironmentDAO;
import com.herowar.dao.game.GeometryDAO;
import com.herowar.dao.game.TowerDAO;
import com.herowar.dao.game.UnitDAO;
import com.herowar.models.entity.game.Environment;
import com.herowar.models.entity.game.GeoMaterial;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.Tower;
import com.herowar.models.entity.game.Unit;
import com.herowar.util.MaterialComparator;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

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
