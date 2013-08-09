package com.herowar.controllers;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


import org.codehaus.jackson.map.ObjectMapper;

import com.herowar.dao.EnvironmentDAO;
import com.herowar.json.excludes.ExcludeGeometryMixin;
import com.herowar.models.entity.game.Environment;

import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Environment controller handle api requests for the Environment model.
 * 
 * @author Sebastian Sachtleben
 */
public class Environments extends BaseAPI<Long, Environment> {

	private static final Logger.ALogger log = Logger.of(Environments.class);

	private Environments() {
		super(Long.class, Environment.class);
	}

	public static final Environments instance = new Environments();

	@Transactional
	public static Result list() {
		log.warn("called listAll without Excludes!");
		return instance.listAll();
	}

	@Transactional
	public static Result root() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Environment.class, ExcludeGeometryMixin.class);
		try {
			return ok(mapper.writeValueAsString(EnvironmentDAO.getInstance().getRootEntity()));
		} catch (IOException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
			log.error("Failed to serialize root environment:", e);
		}
		return badRequest("Unexpected error occurred");
	}

	@Transactional
	public static Result show(Long id) {
		return instance.showEntry(id);
	}

}
