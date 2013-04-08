package models.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.entity.game.GameResult;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.Logger;
import play.data.format.Formats;
import providers.FormSignup;
import providers.SignupUsernamePasswordAuthUser;
import be.objectify.deadbolt.core.models.Subject;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.validation.Email;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import controllers.Application;

/**
 * The User represents each Player for our application.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "users")
@SuppressWarnings("serial")
public class User extends BaseModel implements Subject {

  @Id
  private Long id;

  @Email
  private String email;

  private String username;
  private boolean newsletter = false;
  private boolean active = true;
  private boolean emailValidated = false;

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastLogin;

  @ManyToMany
  @JsonIgnore
  private List<SecurityRole> roles;

  @OneToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  private List<LinkedAccount> linkedAccounts;

  @ManyToMany
  @JsonIgnore
  private List<UserPermission> permissions;

  private Set<GameResult> gameResults;

  private static final Finder<Long, User> finder = new Finder<Long, User>(Long.class, User.class);

  @Override
  public String getIdentifier() {
    // TODO Auto-generated method stub
    return null;
  }

  public static void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
    // TODO Auto-generated method stub
  }

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

  public static void delete(User user) {
    user.deleteManyToManyAssociations("roles");
    user.delete();
  }

  public static User create(String username, String clearPassword, String email) {
    FormSignup form = new FormSignup();
    form.setUsername(username);
    form.setPassword(clearPassword);
    form.setEmail(email);
    SignupUsernamePasswordAuthUser authUser = new SignupUsernamePasswordAuthUser(form);
    return create(authUser);
  }

  public static boolean existsByAuthUserIdentity(AuthUser authUser) {
    // TODO Auto-generated method stub
    return false;
  }

  public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
    final ExpressionList<User> exp;
    if (identity instanceof UsernamePasswordAuthUser) {
      exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
    } else {
      exp = getAuthUserFind(identity);
    }
    return exp.findRowCount() > 0;
  }

  private static ExpressionList<User> getAuthUserFind(final AuthUserIdentity identity) {
    return getFinder().where().eq("active", true).eq("linkedAccounts.providerUserId", identity.getId())
        .eq("linkedAccounts.providerKey", identity.getProvider());
  }

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

  public static void merge(User user, User user2) {
    user.setUsername(user2.getUsername());
    user.setEmail(user2.getEmail());
    user.setActive(user2.isActive());
    user.setNewsletter(user2.isNewsletter());
    user.save();
  }

  public void merge(final User otherUser) {
    for (final LinkedAccount acc : otherUser.linkedAccounts) {
      this.linkedAccounts.add(LinkedAccount.create(acc));
    }
    // do all other merging stuff here - like resources, etc.

    // deactivate the merged user that got added to this one
    otherUser.active = false;
    Ebean.save(Arrays.asList(new User[] { otherUser, this }));
  }

  public static void merge(AuthUser oldUser, AuthUser newUser) {
    User.findByAuthUserIdentity(oldUser).merge(User.findByAuthUserIdentity(newUser));
  }

  public static void setLastLoginDate(AuthUser knownUser) {
    final User u = User.findByAuthUserIdentity(knownUser);
    u.setLastLogin(new Date());
    u.save();
  }

  public static User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
    return getUsernamePasswordAuthUserFind(identity).findUnique();
  }

  private static ExpressionList<User> getUsernamePasswordAuthUserFind(final UsernamePasswordAuthUser identity) {
    return getEmailFind(identity.getEmail()).eq("linkedAccounts.providerKey", identity.getProvider());
  }

  public static User findByUsername(final String username) {
    return getUsernameFind(username).findUnique();
  }

  private static ExpressionList<User> getUsernameFind(final String username) {
    return getFinder().where().eq("active", true).eq("username", username);
  }

  private static ExpressionList<User> getEmailFind(final String email) {
    return getFinder().where().eq("active", true).eq("email", email);
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isNewsletter() {
    return newsletter;
  }

  public void setNewsletter(boolean newsletter) {
    this.newsletter = newsletter;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isEmailValidated() {
    return emailValidated;
  }

  public void setEmailValidated(boolean emailValidated) {
    this.emailValidated = emailValidated;
  }

  public List<LinkedAccount> getLinkedAccounts() {
    return linkedAccounts;
  }

  public void setLinkedAccounts(List<LinkedAccount> linkedAccounts) {
    this.linkedAccounts = linkedAccounts;
  }

  public List<SecurityRole> getRoles() {
    return roles;
  }

  public void setRoles(List<SecurityRole> roles) {
    this.roles = roles;
  }

  public List<UserPermission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<UserPermission> permissions) {
    this.permissions = permissions;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = { CascadeType.ALL })
  @JsonIgnore
  public Set<GameResult> getGameResults() {
    return gameResults;
  }

  public void setGameResults(Set<GameResult> gameResults) {
    this.gameResults = gameResults;
  }

  public static Finder<Long, User> getFinder() {
    return finder;
  }
}
