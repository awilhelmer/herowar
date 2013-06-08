package models.entity.game;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@SuppressWarnings("serial")
public class PlayerSettings implements Serializable {

  @Id
  @GeneratedValue(generator = "playerSettingsGen")
  @GenericGenerator(name = "playerSettingsGen", strategy = "foreign", parameters = @Parameter(name = "property", value="player"))
  private Long id;
  
  @OneToOne(mappedBy = "settings")
  @NotNull
  @JsonIgnore
  private Player player;

  // VIDEO SETTINGS //

  private Boolean displayFPS = true;
  private Integer shadowsQuality = 2;
  private Integer textureQuality = 2;
  private Integer shaderQuality = 2;

  // CONSTRUCTOR //

  public PlayerSettings() {
  }

  public PlayerSettings(Player player) {
    this();
    this.player = player;
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }  
  
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Boolean getDisplayFPS() {
    return displayFPS;
  }

  public void setDisplayFPS(Boolean displayFPS) {
    this.displayFPS = displayFPS;
  }

  public Integer getShadowsQuality() {
    return shadowsQuality;
  }

  public void setShadowsQuality(Integer shadowsQuality) {
    this.shadowsQuality = shadowsQuality;
  }

  public Integer getTextureQuality() {
    return textureQuality;
  }

  public void setTextureQuality(Integer textureQuality) {
    this.textureQuality = textureQuality;
  }

  public Integer getShaderQuality() {
    return shaderQuality;
  }

  public void setShaderQuality(Integer shaderQuality) {
    this.shaderQuality = shaderQuality;
  }
}
