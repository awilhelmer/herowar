import java.util.Arrays;

import models.entity.SecurityRole;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;

import controllers.api.routes;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Call;

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
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Herowar shutdown...");
  }

  private void initialSecurityRoles() {
    if (SecurityRole.getFinder().findRowCount() == 0) {
      for (final String roleName : Arrays.asList(controllers.Application.USER_ROLE, controllers.Application.ADMIN_ROLE)) {
        final SecurityRole role = new SecurityRole();
        role.setRoleName(roleName);
        role.save();
      }
    }
  }

}
