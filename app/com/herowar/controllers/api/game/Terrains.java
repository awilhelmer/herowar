package com.herowar.controllers.api.game;

import static play.libs.Json.toJson;

import com.herowar.controllers.api.BaseAPI;
import com.herowar.models.entity.game.Terrain;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Terrains controller handle api requests for the Terrain model.
 * 
 * @author Sebastian Sachtleben
 */
public class Terrains extends BaseAPI<Long, Terrain> {
	private static final Logger.ALogger log = Logger.of(Terrains.class);

	private Terrains() {
		super(Long.class, Terrain.class);
	}

	public static final Terrains instance = new Terrains();

	@Transactional
	public static Result list() {
		log.warn("called listAll without Excludes!");
		return instance.listAll();
	}

	@Transactional
	public static Result show(Long id) {
		return instance.showEntry(id);
	}

	@Transactional
	public static Result update(Long id) {
		Terrain terrain = instance.findUnique(id);
		terrain = JPA.em().merge(terrain);
		return ok(toJson(terrain));
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
