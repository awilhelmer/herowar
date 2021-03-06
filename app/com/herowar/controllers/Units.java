package com.herowar.controllers;


import java.io.IOException;


import org.codehaus.jackson.map.ObjectMapper;

import com.herowar.dao.UnitDAO;
import com.herowar.json.excludes.ExcludeGeometryMixin;
import com.herowar.models.entity.game.Unit;

import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Units controller handle api requests for the Unit model.
 * 
 * @author Sebastian Sachtleben
 */
public class Units extends BaseAPI<Long, Unit> {

	private static final Logger.ALogger log = Logger.of(Environments.class);

	private Units() {
		super(Long.class, Unit.class);
	}

	public static final Units instance = new Units();

	@Transactional
	public static Result list() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Unit.class, ExcludeGeometryMixin.class);
		try {
			return ok(mapper.writeValueAsString(instance.getAll()));
		} catch (IOException e) {
			log.error("Failed to serialize unit:", e);
		}
		return badRequest("Unexpected error occurred");
	}

	@Transactional
	public static Result root() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Unit.class, ExcludeGeometryMixin.class);
		try {
			Unit root = UnitDAO.getInstance().getByName("Root");
			return ok(mapper.writeValueAsString(root.getChildren()));
		} catch (IOException e) {
			log.error("Failed to serialize root unit:", e);
		}
		return badRequest("Unexpected error occurred");
	}

	@Transactional
	public static Result show(Long id) {
		return instance.showEntry(id);
	}

}
