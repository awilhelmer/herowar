package dao;

import models.entity.game.GameToken;
import models.entity.game.Map;
import models.entity.game.Material;
import models.entity.game.Object3D;

public class GameDAO {
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
    map.save();
  }

  public static void merge(Object3D object, Object3D object2) {
    object.setName(object2.getName());
    object.setDescription(object2.getDescription());
    object.save();
  }

  public static GameToken getTokenById(String token) {
    // TODO Auto-generated method stub
    return null;
  }

  public static Material getMaterialbyId(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  public static Map getMapByName(String string) {
    // TODO Auto-generated method stub
    return null;
  }

  public static int getEnvironmentCount() {
    // TODO Auto-generated method stub
    return 0;
  }
}
