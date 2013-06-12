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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "gameresult")
public class GameResult implements Serializable {

  private static final long serialVersionUID = 1791158983361934175L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long score;
  private Integer lives;
  private Boolean victory;
  private Date cdate;

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "map_id")
  private Map map;

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "player_id")
  @JsonIgnore
  private Player player;

  @OneToOne(mappedBy = "result", optional = false)
  @JsonIgnore
  private GameToken token;

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

  public Integer getLives() {
    return lives;
  }

  public void setLives(Integer lives) {
    this.lives = lives;
  }

  public Date getCdate() {
    return cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public Boolean getVictory() {
    return victory;
  }

  public void setVictory(Boolean victory) {
    this.victory = victory;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public GameToken getToken() {
    return token;
  }

  public void setToken(GameToken token) {
    this.token = token;
  }
}
