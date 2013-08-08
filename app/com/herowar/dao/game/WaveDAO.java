package com.herowar.dao.game;

import com.herowar.dao.BaseDAO;
import com.herowar.models.entity.game.Wave;


public class WaveDAO extends BaseDAO<Long, Wave> {

	private WaveDAO() {
		super(Long.class, Wave.class);
	}

	private static final WaveDAO instance = new WaveDAO();

	public static WaveDAO getInstance() {
		return instance;
	}

}
