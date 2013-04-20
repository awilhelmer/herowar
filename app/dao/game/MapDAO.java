package dao.game;

import models.entity.game.Map;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
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

  @Transactional
  public static void merge(final Map map, final Map map2) {
    map.setName(map2.getName());
    map.setDescription(map2.getDescription());
    map.setTeamSize(map2.getTeamSize());
    JPA.em().persist(map);
  }

  @Transactional
  public static Map getMapByName(String name) {
    return instance.getSingleByPropertyValue("name", name);
  }

  @Transactional
  public static Map getMapById(Long id) {
    return instance.findUnique(id);
  }

}
