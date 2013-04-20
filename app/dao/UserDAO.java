package dao;

import java.util.Collections;
import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import models.entity.LinkedAccount;
import models.entity.User;
import play.Logger;
import play.db.jpa.JPA;
import providers.FormSignup;
import providers.SignupUsernamePasswordAuthUser;

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

  public static void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
    // TODO Auto-generated method stub
  }

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

  public static void delete(User user) {
    JPA.em().remove(user);
  }

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

  public static Object findByUserEmail(String email) {
    return instance.getSingleByPropertyValue("email", email);
  }

  public static void merge(AuthUser oldUser, AuthUser newUser) {
    // findByAuthUserIdentity(oldUser);
    instance.merge(findByAuthUserIdentity(newUser));
    // TODO how to merge?
  }

  public static void setLastLoginDate(AuthUser knownUser) {
    final User u = findByAuthUserIdentity(knownUser);
    u.setLastLogin(new Date());
    // Its attached i hope - no persist
  }

  public static User findByUsername(final String username) {
    return instance.getSingleByPropertyValue("username", username);
  }

  public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    CriteriaQuery<Long> q = builder.createQuery(Long.class);
    Root<User> root = q.from(User.class);
    q.select(builder.count(root));
    Join<User, LinkedAccount> linkedAccounts = root.join("linkedAccounts");
    Predicate precdicate = null;
    if (identity instanceof UsernamePasswordAuthUser) {
      precdicate = getUsernamssePasswordAuthUserFind((UsernamePasswordAuthUser) identity, root, linkedAccounts);
    } else {
      precdicate = getAuthUserFind(identity, root, linkedAccounts);

    }
    q.where(precdicate);
    return JPA.em().createQuery(q).getSingleResult() > 0;
  }

  public static void merge(User user, User user2) {
    user = instance.merge(user);
    user.setUsername(user2.getUsername());
    user.setEmail(user2.getEmail());
    user.setActive(user2.isActive());
    user.setNewsletter(user2.isNewsletter());
  }

  public static User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
    Logger.info("findByUsernamePasswordIdentity(): " + identity.getEmail());
    CriteriaQuery<User> q = instance.getCriteria();
    Root<User> root = instance.getRoot(q);
    Join<User, LinkedAccount> linkedAccounts = root.join("linkedAccounts");
    q.where(getUsernamssePasswordAuthUserFind(identity, root, linkedAccounts));
    try {
      return JPA.em().createQuery(q).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static User findByAuthUserIdentity(final AuthUserIdentity identity) {
    if (identity == null) {
      return null;
    }
    if (identity instanceof UsernamePasswordAuthUser) {
      return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
    } else {
      CriteriaQuery<User> q = instance.getCriteria();
      Root<User> root = instance.getRoot(q);
      Join<User, LinkedAccount> linkedAccounts = root.join("linkedAccounts");
      q.where(getAuthUserFind(identity, root, linkedAccounts));
      try {
        return JPA.em().createQuery(q).getSingleResult();
      } catch (NoResultException e) {
        return null;
      }
    }
  }

  private static Predicate getUsernamssePasswordAuthUserFind(final UsernamePasswordAuthUser identity, Root<User> root, Join<User, LinkedAccount> join) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    return builder.and(builder.equal(root.get("email"), identity.getEmail()), builder.equal(join.get("providerKey"), identity.getProvider()));
  }

  private static Predicate getAuthUserFind(final AuthUserIdentity identity, Root<User> root, Join<User, LinkedAccount> join) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    return builder.and(builder.equal(join.get("providerUserId"), identity.getId()), builder.equal(join.get("providerKey"), identity.getProvider()));
  }

}
