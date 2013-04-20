import java.util.Arrays;

import models.entity.News;
import models.entity.SecurityRole;
import models.entity.game.Map;

import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.mvc.Call;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;

import controllers.api.routes;
import dao.NewsDAO;
import dao.SecurityRoleDAO;
import dao.UserDAO;
import dao.game.MapDAO;
import editor.EnvironmentHandler;
import game.GamesHandler;
import game.network.handler.WebSocketHandler;

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

    JPA.withTransaction(new play.libs.F.Callback0() {
      @Override
      public void invoke() throws Throwable {
        initialSecurityRoles();
        initEventBus();
        EnvironmentHandler.getInstance().sync();
        WebSocketHandler.getInstance();
        GamesHandler.getInstance();
        createAdminUser();
        createTutorialMap();
        createDummyNews();
      }
    });
  }

  private void initEventBus() {
    System.setProperty(EventServiceLocator.SERVICE_NAME_EVENT_BUS, ThreadSafeEventService.class.getName());
    // Init GameHandler
    Logger.info("Eventbus initialized");
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Herowar shutdown...");
    GamesHandler.getInstance().stop();
  }

  private void initialSecurityRoles() {
    if (SecurityRoleDAO.getSecurityRoleCount() != 0) {
      return;
    }
    Logger.info("Creating security roles");
    for (final String roleName : Arrays.asList(controllers.Application.ADMIN_ROLE, controllers.Application.USER_ROLE)) {
      final SecurityRole role = new SecurityRole();
      role.setRoleName(roleName);
      JPA.em().persist(role);
      Logger.info("Save role: " + role.getName());
    }
  }

  private void createAdminUser() {
    if (UserDAO.findByUsername("admin") == null) {
      Logger.info("Admin already exists!");
      return;
    }
    Logger.info("Creating admin user");
    UserDAO.create("admin", "admin", "admin@herowar.com");
  }

  private void createTutorialMap() {
    if (MapDAO.getMapByName("Tutorial") != null) {
      return;
    }
    Logger.info("Creating tutorial map");
    Map tutorialMap = MapDAO.create("Tutorial", "The tutorial map shows new user how to play this game.", 1);
    tutorialMap.getTerrain().setWidth(600);
    tutorialMap.getTerrain().setHeight(600);
    tutorialMap.getTerrain().setSmoothness(0.5f);
    tutorialMap.getTerrain().setzScale(100);
    JPA.em().persist(tutorialMap);
  }

  private void createDummyNews() {
    if (!Play.application().isDev() || NewsDAO.getNewsCount() != 0) {
      return;
    }
    Logger.info("Creating dummy news");
    NewsDAO
        .create(
            "Lorem ipsum dolor sit amet",
            "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
            UserDAO.findByUsername("admin"));
  }

}
