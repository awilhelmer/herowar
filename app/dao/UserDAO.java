package dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.entity.LinkedAccount;
import models.entity.SecurityRole;
import models.entity.UserPermission;
import models.entity.TokenAction.Type;
import models.entity.User;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import providers.FormSignup;
import providers.SignupUsernamePasswordAuthUser;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import controllers.Application;

public class UserDAO extends BaseDAO<Long, User> {
  private UserDAO() {
    super(Long.class, User.class);
  }

  private static final UserDAO instance = new UserDAO();

  @Transactional
  public static void deleteByUser(final User u, final Type type) {

    // TargetActioN!
    Ebean.delete(find.where().eq("targetUser.id", u.getId()).eq("type", type).findIterate());
  }

  @Transactional
  public static void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
    // TODO Auto-generated method stub
  }

  @Transactional
  public static User create(AuthUser authUser) {
    final User user = new User();
    user.setRoles(Collections.singletonList(SecurityRoleDAO.findByRoleName(Application.USER_ROLE)));
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
    JPA.em().persist(user);
    Logger.info("Saved new user " + user.getUsername());
    return user;
  }

  @Transactional
  public static void delete(User user) {
    JPA.em().remove(user);
  }

  @Transactional
  public static boolean existsByAuthUserIdentity(AuthUser authUser) {
    // TODO Auto-generated method stub
    return false;
  }

  public static User create(String username, String clearPassword, String email) {
    FormSignup form = new FormSignup();
    form.setUsername(username);
    form.setPassword(clearPassword);
    form.setEmail(email);
    SignupUsernamePasswordAuthUser authUser = new SignupUsernamePasswordAuthUser(form);
    return create(authUser);
  }

  @Transactional
  public static Object findByUserEmail(String email) {
    return instance.getSingleByPropertyValue("email", email);
  }

  @Transactional
  public static void merge(AuthUser oldUser, AuthUser newUser) {
    // findByAuthUserIdentity(oldUser);
    instance.merge(findByAuthUserIdentity(newUser));
    // TODO how to merge?
  }

  @Transactional
  public static void setLastLoginDate(AuthUser knownUser) {
    final User u = findByAuthUserIdentity(knownUser);
    u.setLastLogin(new Date());
    // Its attached i hope - no persist
  }

  @Transactional
  public static User findByUsername(final String username) {
    return instance.getSingleByPropertyValue("username", username);
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
  public static void merge(User user, User user2) {
    user = instance.merge(user);
    user.setUsername(user2.getUsername());
    user.setEmail(user2.getEmail());
    user.setActive(user2.isActive());
    user.setNewsletter(user2.isNewsletter());
  }

  @Transactional
  public static User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
    return getUsernamePasswordAuthUserFind(identity).findUnique();
  }

  private static ExpressionList<User> getUsernamePasswordAuthUserFind(final UsernamePasswordAuthUser identity) {
    return getEmailFind(identity.getEmail()).eq("linkedAccounts.providerKey", identity.getProvider());
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
}
