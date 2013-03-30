package common.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.format.Formats;
import be.objectify.deadbolt.core.models.Subject;

import com.avaje.ebean.validation.Email;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;

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
  private String password;
  private Boolean newsletter;

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastLogin;

  private boolean active;
  private boolean emailValidated;

  @ManyToMany
  private List<SecurityRole> roles;

  @OneToMany(cascade = CascadeType.ALL)
  private List<LinkedUser> linkedUsers;

  @ManyToMany
  private List<UserPermission> permissions;

  private static final Finder<Long, User> finder = new Finder<Long, User>(Long.class, User.class);

  /**
   * Default constructor.
   */
  public User() {
    super();
  }

  /**
   * Constructor with additional username and password.
   * 
   * @param username
   *          The username to set
   * @param password
   *          The password to set
   */
  public User(String username, String password) {
    this();
    this.username = username;
    this.password = password;
  }

  @Override
  public String getIdentifier() {
    // TODO Auto-generated method stub
    return null;
  }

  public static void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
    // TODO Auto-generated method stub
  }

  public static User create(AuthUser authUser) {
    // TODO Auto-generated method stub
    return null;
  }

  public static boolean existsByAuthUserIdentity(AuthUser authUser) {
    // TODO Auto-generated method stub
    return false;
  }

  public static User findByAuthUserIdentity(AuthUserIdentity u) {
    // TODO Auto-generated method stub
    return null;
  }

  public static void merge(AuthUser oldUser, AuthUser newUser) {
    // TODO Auto-generated method stub
  }

  public static void setLastLoginDate(AuthUser knownUser) {
    // TODO Auto-generated method stub
  }
  
  public static User findByUsernamePasswordIdentity(UsernamePasswordAuthUser user) {
    // TODO Auto-generated method stub
    return null;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getNewsletter() {
    return newsletter;
  }

  public void setNewsletter(Boolean newsletter) {
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

  public List<LinkedUser> getLinkedUsers() {
    return linkedUsers;
  }

  public void setLinkedUsers(List<LinkedUser> linkedUsers) {
    this.linkedUsers = linkedUsers;
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

  public static Finder<Long, User> getFinder() {
    return finder;
  }
}
