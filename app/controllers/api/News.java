package controllers.api;

import static play.libs.Json.toJson;
import play.data.Form;
import play.db.jpa.JPA;
import play.mvc.Result;

/**
 * The News controller handle api requests for the News model.
 * 
 * @author Sebastian Sachtleben
 */
public class News extends BaseAPI<Long, models.entity.News> {

  private News() {
    super(Long.class, models.entity.News.class);
  }

  public static final News instance = new News();

  public static Result list() {
    return instance.listAll();
  }

  public static Result show(Long id) {
    return instance.showEntry(id);
  }

  public static Result update(Long id) {
    //models.entity.News news = instance.findUnique(id);
    models.entity.News news = JPA.em().merge(Form.form(models.entity.News.class).bindFromRequest().get());
    return ok(toJson(news));
  }

  public static Result delete(Long id) {
    return instance.deleteEntry(id);
  }

  public static Result add() {
    return instance.addEntry();
  }
}
