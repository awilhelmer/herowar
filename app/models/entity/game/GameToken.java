package models.entity.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.entity.User;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "gametoken")
public class GameToken implements Serializable {

  private static final long serialVersionUID = -5699234192975949575L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String token;
  private Boolean invalid;
  
  @ManyToOne(cascade = { CascadeType.REFRESH })
  @JoinColumn(name = "createdbyuser_id")
  private User createdByUser;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;
  
  @OneToOne(cascade = { CascadeType.REFRESH }, optional = true)
  private GameResult result;

  public GameToken() {
    this.invalid = false;
  }

  public GameToken(String token) {
    this();
    this.token = token;

  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Boolean getInvalid() {
    return invalid;
  }

  public void setInvalid(Boolean invalid) {
    this.invalid = invalid;
  }

  public User getCreatedByUser() {
    return createdByUser;
  }

  public void setCreatedByUser(User createdByUser) {
    this.createdByUser = createdByUser;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public GameResult getResult() {
    return result;
  }

  public void setResult(GameResult result) {
    this.result = result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((invalid == null) ? 0 : invalid.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GameToken other = (GameToken) obj;
    if (invalid == null) {
      if (other.invalid != null)
        return false;
    } else if (!invalid.equals(other.invalid))
      return false;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "GameToken [token=" + token + ", invalid=" + invalid + "]";
  }

}
