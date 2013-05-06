package dao.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.Map;
import models.entity.game.Material;
import models.entity.game.Mesh;
import models.entity.game.Path;
import models.entity.game.Unit;
import models.entity.game.Wave;
import models.entity.game.Waypoint;
import play.db.jpa.JPA;
import dao.BaseDAO;

public class MapDAO extends BaseDAO<Long, Map> {

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
      wave.setPathId(wave.getPath().getId());
      wave.setUnitIds(new ArrayList<Long>());
      for (Unit unit : wave.getUnits()) {
        wave.getUnitIds().add(unit.getId());
      }
    }

  }

  public static void createWaves(Map map, Set<Long> oldWaveIds, java.util.Map<Long, Path> newPaths) {
    Set<Wave> waves = new HashSet<Wave>();
    for (Wave wave : map.getWaves()) {
      wave.setMap(map);
      Path path = null;
      if (wave.getPathId() != null && wave.getPathId().longValue() > -1) {
        path = PathDAO.getInstance().findUnique(wave.getPathId());
      } else if (wave.getPathId() != null && newPaths != null) {
        path = newPaths.get(wave.getPathId());
      }
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
        oldWaveIds.add(wave.getId());
      } else {
        wave.setId(null);
      }
      waves.add(wave);
    }

    map.setWaves(waves);
  }

  public static java.util.Map<Long, Path> createPaths(Map map, Set<Long> oldPathIds, Set<Long> oldWaypointIds) {
    java.util.Map<Long, Path> result = new HashMap<Long, Path>();
    Set<Path> paths = new HashSet<Path>();
    for (Path path : map.getPaths()) {
      Set<Waypoint> waypoints = new HashSet<Waypoint>();
      for (Waypoint waypoint : path.getWaypoints()) {
        waypoint.setPath(path);
        if (waypoint.getId() != null && waypoint.getId().longValue() > -1) {
          waypoint = JPA.em().merge(waypoint);
          oldWaypointIds.add(waypoint.getId());
        } else {
          waypoint.setId(null);
        }
        waypoints.add(waypoint);
      }

      path.setWaypoints(waypoints);
      path.setMap(map);
      if (path.getId() != null && path.getId().longValue() > -1) {
        path = JPA.em().merge(path);
        oldPathIds.add(path.getId());
      } else {
        result.put(path.getId(), path);
        path.setId(null);
       // JPA.em().persist(path);
      }
      paths.add(path);
    }
    //JPA.em().flush();
    map.setPaths(paths);
    for (Path path : map.getPaths()) {
      path.setMap(map);
    }
    return result;
  }

  public static void createMeshes(Map map, Set<Long> oldMeshIds) {
    Set<Mesh> meshes = new HashSet<Mesh>();
    for (Mesh mesh : map.getObjects()) {
      if (mesh.getGeometry().getId() != null) {
        mesh.setMap(map);
        mesh.setGeometry(GeometryDAO.getInstance().findUnique(mesh.getGeometry().getId()));
      }
      if (mesh.getId() == null || mesh.getId().intValue() < 0) {
        mesh.setId(null);
      } else {
        mesh = MeshDAO.getInstance().merge(mesh);
        oldMeshIds.add(mesh.getId());
      }
      meshes.add(mesh);
    }
    map.setObjects(meshes);
  }

  public static void deletePaths(Map map, Set<Long> oldPathIds, Set<Long> oldWaypointIds) {
    for (Iterator<Path> pathIt = map.getPaths().iterator(); pathIt.hasNext();) {
      Path oldPath = pathIt.next();
      if (!oldPathIds.contains(oldPath.getId())) {
        pathIt.remove();
        JPA.em().remove(oldPath);
      } else {
        for (Iterator<Waypoint> wayIt = oldPath.getWaypoints().iterator(); wayIt.hasNext();) {
          Waypoint oldWaypoint = wayIt.next();
          if (!oldWaypointIds.contains(oldWaypoint.getId())) {
            wayIt.remove();
            JPA.em().remove(oldWaypoint);
          }
        }
      }
    }
  }

  public static void deleteWaves(Map map, Set<Long> oldWaveIds) {
    for (Iterator<Wave> waveIt = map.getWaves().iterator(); waveIt.hasNext();) {
      Wave oldWave = waveIt.next();
      if (!oldWaveIds.contains(oldWave.getId())) {
        waveIt.remove();
        JPA.em().remove(oldWave);
      }
    }
  }

  public static void deleteMeshes(Map map, Set<Long> oldMeshIds) {
    for (Iterator<Mesh> iterator = map.getObjects().iterator(); iterator.hasNext();) {
      Mesh mesh = iterator.next();
      if (!oldMeshIds.contains(mesh.getId())) {
        iterator.remove();
        JPA.em().remove(mesh);
      }

    }
  }
}
