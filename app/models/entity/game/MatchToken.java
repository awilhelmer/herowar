package models.entity.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.entity.BaseModel;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

/**
 * The MatchToken is like an access card for a match. Without a valid gametoken
 * the player cant join the current match.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
public class MatchToken extends BaseModel {

  private static final long serialVersionUID = -5699234192975949575L;

  @Id
  private String token;

  @Type(type = "yes_no")
  private Boolean invalid = Boolean.FALSE;

  @ManyToOne(cascade = { CascadeType.REFRESH })
  @JoinColumn(name = "player_id")
  @JsonIgnore
  private Player player;

  @OneToOne(cascade = { CascadeType.REFRESH }, optional = true)
  @JoinColumn(name = "result_id")
  @JsonIgnore
  private MatchResult result;

  public MatchToken() {
  }

  public MatchToken(String token) {
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

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public MatchResult getResult() {
    return result;
  }

  public void setResult(MatchResult result) {
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
    MatchToken other = (MatchToken) obj;
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
    return "MatchToken [token=" + token + ", invalid=" + invalid + "]";
  }

}
