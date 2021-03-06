package com.herowar.dao;

import java.util.HashMap;
import java.util.List;

import com.herowar.models.entity.game.Material;

import play.db.jpa.JPA;

public class MaterialDAO extends BaseDAO<Long, Material> {

	private MaterialDAO() {
		super(Long.class, Material.class);
	}

	private static final MaterialDAO instance = new MaterialDAO();

	public static Material getMaterialbyId(Long id) {
		return instance.findUnique(id);
	}

	public static Material mergeMaterial(Material material) {
		return instance.merge(material);
	}

	public static java.util.Map<Integer, Material> mapAndSave(List<Material> materials) {
		java.util.Map<Integer, Material> result = new HashMap<Integer, Material>();
		for (int i = 0; i < materials.size(); i++) {
			Material mat = materials.get(i);
			Material dbMat = null;
			if (mat.getDbgName() != null) {
				// For Geometry materials ...
				dbMat = instance.getSingleByPropertyValue("dbgName", mat.getDbgName().trim());
			}
			if (dbMat == null && mat.getId() != null && mat.getId() > -1) {
				dbMat = MaterialDAO.getMaterialbyId(mat.getId());
			}
			if (dbMat == null) {
				if (mat.getId() != null && mat.getId() < 0) {
					mat.setId(null);
					mat.setSortIndex(i);
				}
				dbMat = mat;
				JPA.em().persist(dbMat);
			} else {
				mat.setId(dbMat.getId());
				dbMat = MaterialDAO.mergeMaterial(mat);
			}
			result.put(i, dbMat);
		}
		return result;
	}

}
