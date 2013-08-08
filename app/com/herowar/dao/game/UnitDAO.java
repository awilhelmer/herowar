package com.herowar.dao.game;

import com.herowar.dao.TreeDAO;
import com.herowar.models.entity.game.Unit;


public class UnitDAO extends TreeDAO<Long, Unit> {

	private UnitDAO() {
		super(Long.class, Unit.class);
	}

	private static final UnitDAO instance = new UnitDAO();

	public static UnitDAO getInstance() {
		return instance;
	}

}
