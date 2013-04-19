package dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import controllers.Application;
import play.Logger;
import play.db.jpa.Transactional;
import providers.FormSignup;
import providers.SignupUsernamePasswordAuthUser;
import models.entity.LinkedAccount;
import models.entity.News;
import models.entity.SecurityRole;
import models.entity.TokenAction;
import models.entity.User;
import models.entity.UserPermission;
import models.entity.TokenAction.Type;
import models.entity.game.Map;

public class MainDao {

  @Transactional
  public static LinkedAccount findByProviderKey(final User user, String key) {
    return getFinder().where().eq("user", user).eq("providerKey", key).findUnique();
  }

  @Transactional
  public static void merge(News news, News news2) {
    news.setHeadline(news2.getHeadline());
    news.setText(news2.getText());

  }

  @Transactional
  public static void create(String headline, String text) {
    create(headline, text, Application.getLocalUser());
  }

  @Transactional
  public static void create(String headline, String text, User author) {
    final News news = new News();
    news.setHeadline(headline);
    news.setText(text);
    news.setAuthor(author);
  }

  @Transactional
  public static TokenAction findByToken(final String token, final Type type) {
    return find.where().eq("token", token).eq("type", type).findUnique();
  }

  @Transactional
  public static void deleteByUser(final User u, final Type type) {
    Ebean.delete(find.where().eq("targetUser.id", u.getId()).eq("type", type).findIterate());
  }

  @Transactional
  public static TokenAction create(final Type type, final String token, final User targetUser) {
    final TokenAction ua = new TokenAction();
    ua.targetUser = targetUser;
    ua.token = token;
    ua.type = type;
    final Date created = new Date();
    ua.created = created;
    ua.expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
    ua.save();
    return ua;
  }

  @Transactional
  public static void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
    // TODO Auto-generated method stub
  }

  @Transactional
  public static User create(AuthUser authUser) {
    final User user = new User();
    user.setRoles(Collections.singletonList(SecurityRole.findByRoleName(Application.USER_ROLE)));
    user.setActive(true);
    user.setLastLogin(new Date());
    user.setLinkedAccounts(Collections.singletonList(LinkedAccount.create(authUser)));

    if (authUser instanceof EmailIdentity) {
      final EmailIdentity identity = (EmailIdentity) authUser;
      user.setEmail(identity.getEmail());
      user.setEmailValidated(false);
    }

    if (authUser instanceof NameIdentity) {
      final NameIdentity identity = (NameIdentity) authUser;
      final String name = identity.getName();
      if (name != null) {
        user.setUsername(name);
      }
    }

    user.save();
    user.saveManyToManyAssociations("roles");
    Logger.info("Saved new user " + user.getUsername());
    return user;
  }

  @Transactional
  public static void delete(User user) {
    user.deleteManyToManyAssociations("roles");
    user.delete();
  }

  @Transactional
  public static User create(String username, String clearPassword, String email) {
    FormSignup form = new FormSignup();
    form.setUsername(username);
    form.setPassword(clearPassword);
    form.setEmail(email);
    SignupUsernamePasswordAuthUser authUser = new SignupUsernamePasswordAuthUser(form);
    return create(authUser);
  }

  @Transactional
  public static boolean existsByAuthUserIdentity(AuthUser authUser) {
    // TODO Auto-generated method stub
    return false;
  }

  @Transactional
  public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
    final ExpressionList<User> exp;
    if (identity instanceof UsernamePasswordAuthUser) {
      exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
    } else {
      exp = getAuthUserFind(identity);
    }
    return exp.findRowCount() > 0;
  }

  @Transactional
  private static ExpressionList<User> getAuthUserFind(final AuthUserIdentity identity) {
    return getFinder().where().eq("active", true).eq("linkedAccounts.providerUserId", identity.getId())
        .eq("linkedAccounts.providerKey", identity.getProvider());
  }

  @Transactional
  public static User findByAuthUserIdentity(final AuthUserIdentity identity) {
    if (identity == null) {
      return null;
    }
    if (identity instanceof UsernamePasswordAuthUser) {
      return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
    } else {
      return getAuthUserFind(identity).findUnique();
    }
  }

  @Transactional
  public static void merge(User user, User user2) {
    user.setUsername(user2.getUsername());
    user.setEmail(user2.getEmail());
    user.setActive(user2.isActive());
    user.setNewsletter(user2.isNewsletter());
    user.save();
  }

  @Transactional
  public void merge(final User otherUser) {
    for (final LinkedAccount acc : otherUser.linkedAccounts) {
      this.linkedAccounts.add(LinkedAccount.create(acc));
    }
    // do all other merging stuff here - like resources, etc.

    // deactivate the merged user that got added to this one
    otherUser.active = false;
    Ebean.save(Arrays.asList(new User[] { otherUser, this }));
  }

  @Transactional
  public static void merge(AuthUser oldUser, AuthUser newUser) {
    User.findByAuthUserIdentity(oldUser).merge(User.findByAuthUserIdentity(newUser));
  }

  public static void setLastLoginDate(AuthUser knownUser) {
    final User u = User.findByAuthUserIdentity(knownUser);
    u.setLastLogin(new Date());
    u.save();
  }

  @Transactional
  public static User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
    return getUsernamePasswordAuthUserFind(identity).findUnique();
  }

  @Transactional
  private static ExpressionList<User> getUsernamePasswordAuthUserFind(final UsernamePasswordAuthUser identity) {
    return getEmailFind(identity.getEmail()).eq("linkedAccounts.providerKey", identity.getProvider());
  }

  @Transactional
  public static User findByUsername(final String username) {
    return getUsernameFind(username).findUnique();
  }

  @Transactional
  private static ExpressionList<User> getUsernameFind(final String username) {
    return getFinder().where().eq("active", true).eq("username", username);
  }

  @Transactional
  private static ExpressionList<User> getEmailFind(final String email) {
    return getFinder().where().eq("active", true).eq("email", email);
  }

  @Transactional
  public static UserPermission findByValue(String value) {
    return find.where().eq("value", value).findUnique();
  }

  public static SecurityRole findByRoleName(String roleName) {
    return getFinder().where().eq("roleName", roleName).findUnique();
  }

  public static Map getMapById(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  public static Object findByUserEmail(String email) {
    // TODO Auto-generated method stub
    return null;
  }

  public static int getNewsCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  public static int getSecurityRoleCount() {
    // TODO Auto-generated method stub
    return 0;
  }
}
