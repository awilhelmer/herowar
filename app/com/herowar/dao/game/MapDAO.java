package com.herowar.dao.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.herowar.dao.BaseDAO;
import com.herowar.models.entity.game.GeoMaterial;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.Map;
import com.herowar.models.entity.game.Material;
import com.herowar.models.entity.game.Mesh;
import com.herowar.models.entity.game.Path;
import com.herowar.models.entity.game.Unit;
import com.herowar.models.entity.game.Wave;
import com.herowar.models.entity.game.Waypoint;

import play.Logger;
import play.db.jpa.JPA;

public class MapDAO extends BaseDAO<Long, Map> {
	private static final Logger.ALogger log = Logger.of(MapDAO.class);

	private MapDAO() {
		super(Long.class, Map.class);
	}

	private static final MapDAO instance = new MapDAO();

	public static Map create(String name, String description, int teamSize) {
		final Map map = new Map();
		map.setName(name);
		map.setDescription(description);
		map.setTeamSize(teamSize);
		return map;
	}

	public static void merge(final Map map, final Map map2) {
		map.setName(map2.getName());
		map.setDescription(map2.getDescription());
		map.setTeamSize(map2.getTeamSize());
		JPA.em().persist(map);
	}

	public static Map getMapByName(String name) {
		return instance.getSingleByPropertyValue("name", name);
	}

	public static Map getMapById(Long id) {
		return instance.findUnique(id);
	}

	/**
	 * For loading the MatGeoId.materialId is the real DB id
	 * 
	 * @param map
	 */
	public static void mapMaterials(Map map) {
		Geometry geo = map.getTerrain().getGeometry();
		map.setMaterials(new ArrayList<Material>());
		if (map.getAllMaterials() != null) {
			for (Material mat : map.getAllMaterials()) {
				mat.setMaterialId(mat.getId());
				map.getMaterials().add(mat);
			}

			Collections.sort(map.getMaterials(), new Comparator<Material>() {
				@Override
				public int compare(Material o1, Material o2) {
					return o1.getSortIndex().compareTo(o2.getSortIndex());
				}

			});
			GeometryDAO.mapMaterials(geo);
		}
	}

	public static void mapStaticGeometries(Map map) {
		map.setStaticGeometries(new ArrayList<Geometry>());
		for (Mesh mesh : map.getObjects()) {
			mesh.setGeoId(mesh.getGeometry().getId());
			if (!map.getStaticGeometries().contains(mesh.getGeometry()))
				mesh.getGeometry().setMaterials(new ArrayList<Material>());
			for (GeoMaterial geoMap : mesh.getGeometry().getGeoMaterials()) {
				mesh.getGeometry().getMaterials().add(geoMap.getId().getMaterial());
			}
			GeometryDAO.mapMaterials(mesh.getGeometry());
			map.getStaticGeometries().add(mesh.getGeometry());
		}
	}

	public static void mapWaves(Map map) {
		for (Wave wave : map.getWaves()) {
			if (wave.getPath() != null)
				wave.setPathId(wave.getPath().getId());
			wave.setUnitIds(new ArrayList<Long>());
			for (Unit unit : wave.getUnits()) {
				wave.getUnitIds().add(unit.getId());
			}
		}

	}

	public static void createWaves(Map map, java.util.Map<Long, Path> newPaths) {
		Set<Wave> waves = new HashSet<Wave>();
		for (Wave wave : map.getWaves()) {
			wave.setMap(map);
			Path path = null;
			if (wave.getPathId() != null && wave.getPathId().longValue() > -1) {
				path = PathDAO.getInstance().findUnique(wave.getPathId());
			} else if (wave.getPathId() != null && newPaths != null) {
				path = newPaths.get(wave.getPathId());
			}
			log.info("saving wave with pathId " + wave.getPathId());
			wave.setPath(path);
			wave.setUnits(new HashSet<Unit>());
			if (wave.getUnitIds() != null) {
				for (Long unitId : wave.getUnitIds()) {
					Unit unit = UnitDAO.getInstance().findUnique(unitId);
					if (unit != null)
						wave.getUnits().add(unit);
				}
			}
			if (wave.getId() != null && wave.getId().longValue() > -1) {
				wave = JPA.em().merge(wave);
			} else {
				wave.setId(null);
			}
			waves.add(wave);
		}

		map.setWaves(waves);

	}

	public static java.util.Map<Long, Path> createPaths(Map map) {
		java.util.Map<Long, Path> result = new HashMap<Long, Path>();
		Set<Path> paths = new HashSet<Path>();
		for (Path path : map.getPaths()) {
			path.setMap(map);
			Set<Waypoint> waypoints = new HashSet<Waypoint>();
			for (int i = 0; i < path.getWaypoints().size(); i++) {
				Waypoint waypoint = path.getWaypoints().get(i);
				waypoint.setSortOder(i);
				waypoint.setPath(path);
				if (waypoint.getId() != null && waypoint.getId().longValue() > -1) {
					waypoint = JPA.em().merge(waypoint);
				} else {
					waypoint.setId(null);
				}
				waypoints.add(waypoint);
			}

			path.setDbWaypoints(waypoints);
			if (path.getId() != null && path.getId().longValue() > -1) {
				path = JPA.em().merge(path);
			} else {
				result.put(path.getId(), path);
				path.setId(null);

			}
			paths.add(path);
		}

		map.setPaths(paths);

		return result;
	}

	public static void createMeshes(Map map) {
		Set<Mesh> meshes = new HashSet<Mesh>();
		for (Mesh mesh : map.getObjects()) {
			mesh.setMap(map);
			if (mesh.getGeometry().getId() != null) {
				mesh.setGeometry(GeometryDAO.getInstance().findUnique(mesh.getGeometry().getId()));
			}
			if (mesh.getId() == null || mesh.getId().intValue() < 0) {
				mesh.setId(null);
				JPA.em().persist(mesh);
			} else {
				mesh = MeshDAO.getInstance().merge(mesh);
			}
			meshes.add(mesh);
		}

		map.setObjects(meshes);

	}

	public static void mapPaths(Map map) {
		for (Path path : map.getPaths()) {
			PathDAO.mapWaypoints(path);
		}

	}

	public static void mapAll(Map map) {
		mapMaterials(map);
		mapStaticGeometries(map);
		mapWaves(map);
		mapPaths(map);

	}

}
