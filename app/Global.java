import game.GamesHandler;
import game.network.handler.WebSocketHandler;

import java.util.Arrays;

import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

import models.entity.News;
import models.entity.SecurityRole;
import models.entity.User;
import models.entity.game.Map;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Call;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;

import controllers.api.routes;

/**
 * Handles global settings.
 * 
 * @author Sebastian Sachtleben
 */
public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Herowar has stated");
    PlayAuthenticate.setResolver(new Resolver() {

      @Override
      public Call login() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Call auth(String arg0) {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Call askMerge() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Call askLink() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Call afterLogout() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Call afterAuth() {
        return routes.Me.show();
      }
    });

    initialSecurityRoles();
    initEventBus();
    WebSocketHandler.getInstance();
    createAdminUser();
    createTutorialMap();
    createDummyNews(app);

  }

  private void initEventBus() {
    System.setProperty(EventServiceLocator.SERVICE_NAME_EVENT_BUS, ThreadSafeEventService.class.getName());
    // Init GameHandler
    GamesHandler.getInstance();
    Logger.info("Eventbus initialized...");
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Herowar shutdown...");
    GamesHandler.getInstance().stop();
    WebSocketHandler.getInstance().destory();
  }

  private void initialSecurityRoles() {
    if (SecurityRole.getFinder().findRowCount() != 0) {
      return;
    }
    Logger.info("Creating security roles");
    for (final String roleName : Arrays.asList(controllers.Application.ADMIN_ROLE, controllers.Application.USER_ROLE)) {
      final SecurityRole role = new SecurityRole();
      role.setRoleName(roleName);
      role.save();
      Logger.info("Save role: " + role.getName());
    }
  }

  private void createAdminUser() {
    if (User.getFinder().where().eq("username", "admin").findUnique() != null) {
      return;
    }
    Logger.info("Creating admin user");
    User.create("admin", "admin", "admin@herowar.com");
  }

  private void createTutorialMap() {
    if (Map.getFinder().where().eq("name", "Tutorial").findUnique() != null) {
      return;
    }
    Logger.info("Creating tutorial map");
    Map.create("Tutorial", "The tutorial map shows new user how to play this game.", 1);
  }

  private void createDummyNews(Application app) {
    if (!app.isDev() || News.getFinder().findRowCount() != 0) {
      return;
    }
    Logger.info("Creating dummy news");
    News.create(
        "Lorem ipsum dolor sit amet",
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
        User.findByUsername("admin"));
  }

}
