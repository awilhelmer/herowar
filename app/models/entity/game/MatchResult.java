package models.entity.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The PlayerMatchResult contains information about player specific details for
 * a match.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class MatchResult implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long score = 0L;

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "player_id")
  private Player player;

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "token_id")
  @JsonIgnore
  private MatchToken token;

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "match_id")
  @JsonIgnore
  private Match match;

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

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public MatchToken getToken() {
    return token;
  }

  public void setToken(MatchToken token) {
    this.token = token;
  }

  public Match getMatch() {
    return match;
  }

  public void setMatch(Match match) {
    this.match = match;
  }

  @Override
  public String toString() {
    return "MatchResult [id=" + id + ", score=" + score + "]";
  }
}
