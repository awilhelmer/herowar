package common.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

import com.feth.play.module.pa.user.AuthUser;

@Entity
@SuppressWarnings("serial")
public class LinkedAccounts extends Model {

  @Id
  private Long id;

  @ManyToOne
  private User user;

  private String providerUserId;
  private String providerKey;

  public static final Finder<Long, LinkedAccounts> finder = new Finder<Long, LinkedAccounts>(Long.class, LinkedAccounts.class);

  public static LinkedAccounts findByProviderKey(final User user, String key) {
    return getFinder().where().eq("user", user).eq("providerKey", key).findUnique();
  }

  public static LinkedAccounts create(final AuthUser authUser) {
    final LinkedAccounts ret = new LinkedAccounts();
    ret.update(authUser);
    return ret;
  }

  public void update(final AuthUser authUser) {
    this.providerKey = authUser.getProvider();
    this.providerUserId = authUser.getId();
  }

  public static LinkedAccounts create(final LinkedAccounts acc) {
    final LinkedAccounts ret = new LinkedAccounts();
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

  public static Finder<Long, LinkedAccounts> getFinder() {
    return finder;
  }

}
