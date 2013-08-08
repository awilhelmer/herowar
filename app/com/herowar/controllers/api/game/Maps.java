package com.herowar.controllers.api.game;

import static play.libs.Json.toJson;

import java.io.IOException;
import java.util.List;


import org.codehaus.jackson.map.ObjectMapper;

import com.herowar.controllers.api.BaseAPI;
import com.herowar.game.json.excludes.MapDataExcludeMixin;
import com.herowar.models.entity.game.Map;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Maps controller handle api requests for the Map model.
 * 
 * @author Sebastian Sachtleben
 */
public class Maps extends BaseAPI<Long, Map> {
	private static final Logger.ALogger log = Logger.of(Maps.class);

	private Maps() {
		super(Long.class, Map.class);
	}

	public static final Maps instance = new Maps();

	@Transactional
	public static Result list() {
		List<Map> result = instance.getAll();
		for (Map map : result) {
			JPA.em().detach(map);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Map.class, MapDataExcludeMixin.class);
		try {
			return ok(mapper.writeValueAsString(result));
		} catch (IOException e) {
			log.error("Failed to serialize root environment:", e);
		}

		return badRequest("Unexpected error occurred");

	}

	@Transactional
	public static Result show(Long id) {
		Map map = instance.findUnique(id);

		return ok(toJson(map));
	}

	@Transactional
	public static Result update(Long id) {
		Map map = instance.findUnique(id);
		map = JPA.em().merge(map);
		return ok(toJson(map));
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
