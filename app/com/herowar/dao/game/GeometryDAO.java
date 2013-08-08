package com.herowar.dao.game;

import java.util.ArrayList;
import java.util.HashSet;

import com.herowar.dao.BaseDAO;
import com.herowar.models.entity.game.GeoMaterial;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.Material;


/**
 * @author Sebastian Sachtleben
 */
public class GeometryDAO extends BaseDAO<Long, Geometry> {
	private GeometryDAO() {
		super(Long.class, Geometry.class);
	}

	private static final GeometryDAO instance = new GeometryDAO();

	public static GeometryDAO getInstance() {
		return instance;
	}

	/**
	 * For mapping geometry in Client
	 * 
	 * @param Geometry
	 */
	public static void mapMaterials(Geometry geo) {
		geo.setMatIdMapper(new ArrayList<GeoMatId>());
		for (GeoMaterial geoMat : geo.getGeoMaterials()) {
			Material mat = geoMat.getId().getMaterial();
			GeoMatId id = new GeoMatId();
			id.setMaterialIndex(geoMat.getArrayIndex());
			id.setMaterialId(mat.getId());
			id.setMaterialName(mat.getName());
			geo.getMatIdMapper().add(id);
		}
	}

	public static void createGeoMaterials(Geometry geometry, java.util.Map<Integer, Material> mapping) {
		if (geometry.getGeoMaterials() == null)
			geometry.setGeoMaterials(new HashSet<GeoMaterial>());
		for (Integer index : mapping.keySet()) {
			Material mat = mapping.get(index);
			GeoMaterial geoMat = new GeoMaterial();
			geoMat.setId(new GeoMaterial.PK());
			geoMat.getId().setMaterial(mat);
			geoMat.getId().setGeometry(geometry);
			geoMat.setArrayIndex(index.longValue());
			geometry.getGeoMaterials().add(geoMat);
		}
	}
}
