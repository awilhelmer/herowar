package com.herowar.dao;

import com.herowar.models.entity.game.Mesh;


public class MeshDAO extends BaseDAO<Long, Mesh> {

	private MeshDAO() {
		super(Long.class, Mesh.class);
	}

	private static final MeshDAO instance = new MeshDAO();

	public static MeshDAO getInstance() {
		return instance;
	}
}
