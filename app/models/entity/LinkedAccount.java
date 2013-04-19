package models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.feth.play.module.pa.user.AuthUser;

@Entity
@SuppressWarnings("serial")
public class LinkedAccount implements Serializable {

  @Id
  private Long id;

  @ManyToOne
  private User user;

  private String providerUserId;
  private String providerKey;

  public static LinkedAccount create(final AuthUser authUser) {
    final LinkedAccount ret = new LinkedAccount();
    ret.update(authUser);
    return ret;
  }

  public void update(final AuthUser authUser) {
    this.providerKey = authUser.getProvider();
    this.providerUserId = authUser.getId();
  }

  public static LinkedAccount create(final LinkedAccount acc) {
    final LinkedAccount ret = new LinkedAccount();
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
