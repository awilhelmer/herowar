package models.entity.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.entity.User;


/**
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "gameresult")
public class GameResult  implements Serializable {

  private static final long serialVersionUID = 1791158983361934175L;
  
  private Long id;
  private Long score;
  private Integer health;
  private Integer kills;
  private Integer shots;
  private Integer accuracy;
  private Boolean victory;
  private User user;
  private GameToken token;
  

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getScore() {
    return score;
  }
  
  public void setScore(Long score) {
    this.score = score;
  }
  
  public Integer getHealth() {
    return health;
  }
  
  public void setHealth(Integer health) {
    this.health = health;
  }
  
  public Integer getKills() {
    return kills;
  }
  
  public void setKills(Integer kills) {
    this.kills = kills;
  }
  
  public Integer getShots() {
    return shots;
  }

  public void setShots(Integer shots) {
    this.shots = shots;
  }

  public Integer getAccuracy() {
    return accuracy;
  }
  
  public void setAccuracy(Integer accuracy) {
    this.accuracy = accuracy;
  }
  
  public Boolean getVictory() {
    return victory;
  }

  public void setVictory(Boolean victory) {
    this.victory = victory;
  }

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "user_id")
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  
  @OneToOne(mappedBy="result", optional = false)
  public GameToken getToken() {
    return token;
  }

  public void setToken(GameToken token) {
    this.token = token;
  }

  @Transient
  public Long getHealthBonus() {
    return ((long) kills * (long) health);
  }
  
  @Transient
  public Long getKillsBonus() {
    return kills * 100l;
  }
  
  @Transient
  public Long getAccuracyBonus() {
    return ((long) accuracy * (long)kills);
  }
  
  @Transient
  public Long getTotalScore() {
    return getScore() + getHealthBonus() + getKillsBonus() + getAccuracyBonus();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accuracy == null) ? 0 : accuracy.hashCode());
    result = prime * result + ((health == null) ? 0 : health.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((kills == null) ? 0 : kills.hashCode());
    result = prime * result + ((score == null) ? 0 : score.hashCode());
    result = prime * result + ((shots == null) ? 0 : shots.hashCode());
    result = prime * result + ((victory == null) ? 0 : victory.hashCode());
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
    GameResult other = (GameResult) obj;
    if (accuracy == null) {
      if (other.accuracy != null)
        return false;
    } else if (!accuracy.equals(other.accuracy))
      return false;
    if (health == null) {
      if (other.health != null)
        return false;
    } else if (!health.equals(other.health))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (kills == null) {
      if (other.kills != null)
        return false;
    } else if (!kills.equals(other.kills))
      return false;
    if (score == null) {
      if (other.score != null)
        return false;
    } else if (!score.equals(other.score))
      return false;
    if (shots == null) {
      if (other.shots != null)
        return false;
    } else if (!shots.equals(other.shots))
      return false;
    if (victory == null) {
      if (other.victory != null)
        return false;
    } else if (!victory.equals(other.victory))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "GameResult [id=" + id + ", score=" + score + ", health=" + health + ", kills=" + kills + ", shots=" + shots + ", accuracy=" + accuracy
        + ", victory=" + victory + "]";
  }
  
}
