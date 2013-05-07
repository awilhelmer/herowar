package dao.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.entity.game.Path;
import models.entity.game.Waypoint;
import dao.BaseDAO;

public class PathDAO extends BaseDAO<Long, Path> {

  private PathDAO() {
    super(Long.class, Path.class);
  }

  private static final PathDAO instance = new PathDAO();

  public static PathDAO getInstance() {
    return instance;
  }

  public static void mapWaypoints(Path path) {
    List<Waypoint> waypoints = new ArrayList<Waypoint>();

    for (Waypoint point : path.getDbWaypoints()) {
      waypoints.add(point);
    }
    Collections.sort(waypoints, new Comparator<Waypoint>() {

      @Override
      public int compare(Waypoint o1, Waypoint o2) {
        return o1.getSortOder().compareTo(o2.getSortOder());
      }

    });

    path.setWaypoints(waypoints);

  }

}
