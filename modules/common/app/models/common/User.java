package models.common;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.Email;

import play.data.format.Formats;

/**
 * The User represents each Player for our application.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "users")
@SuppressWarnings("serial")
public class User extends BaseModel {

  @Id
  private Long id;
  
  @Email
  private String email;
  
  private String username;
  private String password;
  private Boolean newsletter;

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastLogin;
  
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
}
