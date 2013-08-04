package dao.game;

import models.entity.game.Unit;
import dao.TreeDAO;

public class UnitDAO extends TreeDAO<Long, Unit> {

	private UnitDAO() {
		super(Long.class, Unit.class);
	}

	private static final UnitDAO instance = new UnitDAO();

	public static UnitDAO getInstance() {
		return instance;
	}

}
