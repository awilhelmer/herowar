package models.entity;

import java.io.Serializable;
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

import play.data.format.Formats;
import be.objectify.deadbolt.core.models.Subject;

import com.avaje.ebean.validation.Email;

/**
 * The User represents each Player for our application.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "users")
@SuppressWarnings("serial")
public class User implements Subject, Serializable {

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

  @ManyToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  private List<SecurityRole> roles;

  @OneToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  private List<LinkedAccount> linkedAccounts;

  @ManyToMany
  @JsonIgnore
  private List<UserPermission> permissions;

  private Set<GameResult> gameResults;

  @Override
  public String getIdentifier() {
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

}
