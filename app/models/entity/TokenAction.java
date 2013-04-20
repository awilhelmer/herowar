package models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;

import com.avaje.ebean.annotation.EnumValue;

@Entity
@SuppressWarnings("serial")
public class TokenAction implements Serializable {

  public enum Type {
    @EnumValue("EV")
    EMAIL_VERIFICATION,

    @EnumValue("PR")
    PASSWORD_RESET
  }

  /**
   * Verification time frame (until the user clicks on the link in the email) in
   * seconds Defaults to one week
   */
  public final static long VERIFICATION_TIME = 7 * 24 * 3600;

  @Id
  private Long id;

  @Column(unique = true)
  private String token;

  @ManyToOne
  private User targetUser;

  private Type type;

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date created;

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date expires;

  @javax.persistence.Transient
  public boolean isValid() {
    return this.expires.after(new Date());
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getTargetUser() {
    return targetUser;
  }

  public void setTargetUser(User targetUser) {
    this.targetUser = targetUser;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(Date expires) {
    this.expires = expires;
  }
}
