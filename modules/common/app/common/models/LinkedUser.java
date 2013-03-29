package common.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

import com.feth.play.module.pa.user.AuthUser;

@Entity
@SuppressWarnings("serial")
public class LinkedUser extends Model {

  @Id
  private Long id;

  @ManyToOne
  private User user;

  private String providerUserId;
  private String providerKey;

  public static final Finder<Long, LinkedUser> find = new Finder<Long, LinkedUser>(Long.class, LinkedUser.class);

  public static LinkedUser findByProviderKey(final User user, String key) {
    return find.where().eq("user", user).eq("providerKey", key).findUnique();
  }

  public static LinkedUser create(final AuthUser authUser) {
    final LinkedUser ret = new LinkedUser();
    ret.update(authUser);
    return ret;
  }

  public void update(final AuthUser authUser) {
    this.providerKey = authUser.getProvider();
    this.providerUserId = authUser.getId();
  }

  public static LinkedUser create(final LinkedUser acc) {
    final LinkedUser ret = new LinkedUser();
    ret.providerKey = acc.providerKey;
    ret.providerUserId = acc.providerUserId;

    return ret;
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getProviderUserId() {
    return providerUserId;
  }

  public void setProviderUserId(String providerUserId) {
    this.providerUserId = providerUserId;
  }

  public String getProviderKey() {
    return providerKey;
  }

  public void setProviderKey(String providerKey) {
    this.providerKey = providerKey;
  }

}
