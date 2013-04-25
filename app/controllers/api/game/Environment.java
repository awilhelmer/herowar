package controllers.api.game;

import game.json.EnvironmentExcludeGeoMixin;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.api.BaseAPI;
import dao.game.EnvironmentDAO;

/**
 * The Environment controller handle api requests for the Environment model.
 * 
 * @author Sebastian Sachtleben
 */
public class Environment extends BaseAPI<Long, models.entity.game.Environment> {

  private static final Logger.ALogger log = Logger.of(Environment.class);
  
  private Environment() {
    super(Long.class, models.entity.game.Environment.class);
  }

  public static final Environment instance = new Environment();

  @Transactional
  public static Result list() {
    return instance.listAll(); 
  }
  
  @Transactional
  public static Result root() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.getSerializationConfig().addMixInAnnotations(models.entity.game.Environment.class, EnvironmentExcludeGeoMixin.class);
    try {
      return ok(mapper.writeValueAsString(EnvironmentDAO.getRoot()));
    } catch (IOException e) {
      log.error("Failed to serialize root environment:", e);
    }
    return badRequest("Unexpected error occurred");
  }

  @Transactional
  public static Result show(Long id) {
    return instance.showEntry(id);
  }

}
