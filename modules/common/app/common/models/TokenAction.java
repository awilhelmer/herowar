package common.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.EnumValue;

@Entity
@SuppressWarnings("serial")
public class TokenAction extends Model {

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
  private final static long VERIFICATION_TIME = 7 * 24 * 3600;

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

  private static final Finder<Long, TokenAction> find = new Finder<Long, TokenAction>(Long.class, TokenAction.class);

  public static TokenAction findByToken(final String token, final Type type) {
    return find.where().eq("token", token).eq("type", type).findUnique();
  }

  public static void deleteByUser(final User u, final Type type) {
    Ebean.delete(find.where().eq("targetUser.id", u.getId()).eq("type", type).findIterate());
  }

  public boolean isValid() {
    return this.expires.after(new Date());
  }

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
