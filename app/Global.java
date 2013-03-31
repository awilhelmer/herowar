import java.util.Arrays;

import models.entity.SecurityRole;
import models.entity.User;
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
    createAdminUser();
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Herowar shutdown...");
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
    if(User.getFinder().where().eq("username", "admin").findUnique() != null) {
      return;
    }
    Logger.info("Creating admin user");
    User.create("admin", "admin", "admin@herowar.com");
  }

}
