import java.util.Arrays;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;

import play.Application;
import play.GlobalSettings;
import play.mvc.Call;

import common.models.entity.SecurityRole;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
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
        // TODO Auto-generated method stub
        return null;
      }
    });
    
    initialSecurityRoles();
  }

  private void initialSecurityRoles() {
    if (SecurityRole.getFinder().findRowCount() == 0) {
      for (final String roleName : Arrays.asList(common.controllers.Application.USER_ROLE, common.controllers.Application.ADMIN_ROLE)) {
        final SecurityRole role = new SecurityRole();
        role.setRoleName(roleName);
        role.save();
      }
    }
  }

}
