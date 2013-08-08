package com.herowar.controllers.api;

import static play.libs.Json.toJson;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.herowar.dao.game.EnvironmentDAO;
import com.herowar.dao.game.GeometryDAO;
import com.herowar.dao.game.MapDAO;
import com.herowar.dao.game.MaterialDAO;
import com.herowar.game.json.excludes.MeshExcludeGeometryMixin;
import com.herowar.models.entity.game.Environment;
import com.herowar.models.entity.game.GeoMaterial;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.GeometryType;
import com.herowar.models.entity.game.Map;
import com.herowar.models.entity.game.Material;
import com.herowar.models.entity.game.Mesh;
import com.herowar.models.entity.game.Path;
import com.herowar.util.EnvironmentComparator;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class Editor extends Controller {

	private static final Logger.ALogger log = Logger.of(Editor.class);

	public static Result mapDefault() {
		Map map = new Map();
		map.getTerrain().getGeometry().setType(GeometryType.TERRAIN);
		map.getTerrain().getGeometry().getMetadata().setGeneratedBy("WorldEditor");
		return ok(toJson(map));
	}

	@Transactional
	public static Result mapShow(Long id) {
		Map map = MapDAO.getMapById(id);
		if (map != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.getSerializationConfig().addMixInAnnotations(Mesh.class, MeshExcludeGeometryMixin.class);
			MapDAO.mapAll(map);
			try {
				return ok(mapper.writeValueAsString(map));
			} catch (IOException e) {
				log.error("Failed to serialize root environment:", e);
			}

			return badRequest("Unexpected error occurred");
		}
		return notFound();
	}

	@Transactional
	public static Result envShow(Long id) {
		List<Environment> envs = EnvironmentDAO.getEnvWithGeometries(id);
		Collections.sort(envs, new EnvironmentComparator());
		return ok(toJson(envs));
	}

	@BodyParser.Of(value = BodyParser.Json.class, maxLength = 52428800)
	@Transactional
	public static Result addMap() {
		log.info("Saving MAP");
		if (request().body().isMaxSizeExceeded()) {
			return badRequest("Too much data!");
		}
		JsonNode mapNode = request().body().asJson();
		if (mapNode == null) {
			return badRequest("Failed to parse json request");
		}
		ObjectMapper mapper = new ObjectMapper();
		Map map = null;
		try {
			map = mapper.readValue(mapNode, Map.class);
		} catch (IOException e) {
			String errorMessage = "Failed to parse request data to entity";
			log.error(errorMessage, e);
			return badRequest(errorMessage);
		}
		if (map == null || !isValid(map)) {
			String errorMessage = "Failed to parse request data to entity";
			return badRequest(errorMessage);
		}
		saveMap(map);
		// Flushing for new Id
		JPA.em().flush();
		log.info(String.format("Map Id <%s> successfully saved!", map.getId()));
		return ok(toJson(map.getId()));
	}

	private static boolean isValid(Map map) {
		if (map == null || map.getTerrain() == null || map.getTerrain().getGeometry() == null
				|| map.getTerrain().getGeometry().getMetadata() == null) {
			return false;
		}
		return true;
	}

	private static void saveMap(Map map) {

		if (map.getTerrain().getGeometry().getMetadata().getGeometry() == null) {
			map.getTerrain().getGeometry().getMetadata().setGeometry(map.getTerrain().getGeometry());
		}
		if (map.getTerrain().getGeometry().getType() == null) {
			map.getTerrain().getGeometry().setType(GeometryType.TERRAIN);
		}
		if (map.getTerrain().getMap() == null) {
			map.getTerrain().setMap(map);
		}
		if (map.getObjects() != null) {
			MapDAO.createMeshes(map);
		}

		java.util.Map<Long, Path> newPaths = null;
		if (map.getPaths() != null) {
			newPaths = MapDAO.createPaths(map);
		}
		if (map.getWaves() != null) {
			MapDAO.createWaves(map, newPaths);
		}

		// For saving MatGeoId.materialId is the index of map.getMaterials()!
		java.util.Map<Integer, Material> matMap = saveMaterials(map);
		saveGeometryMaterials(map.getTerrain().getGeometry(), matMap);
		if (map.getId() == null) {
			JPA.em().persist(map);
		} else {
			map = JPA.em().merge(map);
		}

	}

	// Mapping Indexes
	private static void saveGeometryMaterials(Geometry geometry, java.util.Map<Integer, Material> mapping) {
		if (geometry.getMatIdMapper() != null) {
			if (geometry.getGeoMaterials() == null) {
				geometry.setGeoMaterials(new HashSet<GeoMaterial>());
			}
			GeometryDAO.createGeoMaterials(geometry, mapping);
		}
	}

	private static java.util.Map<Integer, Material> saveMaterials(Map map) {

		if (map.getAllMaterials() == null) {
			map.setAllMaterials(new HashSet<Material>());
		}
		java.util.Map<Integer, Material> result = MaterialDAO.mapAndSave(map.getMaterials());
		for (Material mat : result.values()) {
			map.getAllMaterials().add(mat);
		}
		return result;
	}

}
