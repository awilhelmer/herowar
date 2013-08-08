package com.herowar.controllers.api.game;

import static play.libs.Json.toJson;

import java.io.IOException;


import org.codehaus.jackson.map.ObjectMapper;

import com.herowar.controllers.api.BaseAPI;
import com.herowar.game.json.excludes.ExcludeGeometryMixin;
import com.herowar.models.entity.game.Tower;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Towers controller handle api requests for the Tower model.
 * 
 * @author Sebastian Sachtleben
 */
public class Towers extends BaseAPI<Long, Tower> {
	private static final Logger.ALogger log = Logger.of(Towers.class);

	private Towers() {
		super(Long.class, Tower.class);
	}

	public static final Towers instance = new Towers();

	@Transactional
	public static Result list() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Tower.class, ExcludeGeometryMixin.class);
		try {
			return ok(mapper.writeValueAsString(instance.getAll()));
		} catch (IOException e) {
			log.error("Failed to serialize tower:", e);
		}
		return badRequest("Unexpected error occurred");
	}

	@Transactional
	public static Result show(Long id) {
		return instance.showEntry(id);
	}

	@Transactional
	public static Result update(Long id) {
		Tower tower = instance.findUnique(id);
		tower = JPA.em().merge(tower);
		return ok(toJson(tower));
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
