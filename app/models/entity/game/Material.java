package models.entity.game;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@Table(name = "material", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Material implements Serializable {
  private static final long serialVersionUID = 1651915135235L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String color;
  // They are for model imports
  private Integer DbgColor;
  private Integer DbgIndex;
  private String DbgName;

  private String blending;
  private Boolean depthTest;
  private Boolean depthWrite;
  private String mapDiffuseWrap;
  private String shading;
  private Integer specularCoef;
  @Column(scale = 3)
  private Float transparency;
  private Boolean vertexColors;
  private Boolean transparent;

  private String colorAmbient;
  private String colorDiffuse;
  private String colorSpecular;
  private String mapDiffuse;

  @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
  private Texture texture;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "allMaterials")
  @JsonIgnore
  private Set<Map> maps;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "id.material")
  @JsonIgnore
  private Set<GeoMaterial> geoMaterials;

  @Column(precision = 2)
  private Float opacity;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name="name", length = 100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Boolean getTransparent() {
    return transparent;
  }

  public void setTransparent(Boolean transparent) {
    this.transparent = transparent;
  }

  public Float getOpacity() {
    return opacity;
  }

  public void setOpacity(Float opacity) {
    this.opacity = opacity;
  }

  public Set<Map> getMaps() {
    return maps;
  }

  public void setMaps(Set<Map> maps) {
    this.maps = maps;
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public Set<GeoMaterial> getGeoMaterials() {
    return geoMaterials;
  }

  public void setGeoMaterials(Set<GeoMaterial> geoMaterials) {
    this.geoMaterials = geoMaterials;
  }

  public Integer getDbgColor() {
    return DbgColor;
  }

  public void setDbgColor(Integer dbgColor) {
    DbgColor = dbgColor;
  }

  public Integer getDbgIndex() {
    return DbgIndex;
  }

  public void setDbgIndex(Integer dbgIndex) {
    DbgIndex = dbgIndex;
  }

  public String getDbgName() {
    return DbgName;
  }

  public void setDbgName(String dbgName) {
    DbgName = dbgName;
  }

  public String getColorAmbient() {
    return colorAmbient;
  }

  public void setColorAmbient(String colorAmbient) {
    this.colorAmbient = colorAmbient;
  }

  public String getColorDiffuse() {
    return colorDiffuse;
  }

  public void setColorDiffuse(String colorDiffuse) {
    this.colorDiffuse = colorDiffuse;
  }

  public String getColorSpecular() {
    return colorSpecular;
  }

  public void setColorSpecular(String colorSpecular) {
    this.colorSpecular = colorSpecular;
  }

  public String getMapDiffuse() {
    return mapDiffuse;
  }

  public void setMapDiffuse(String mapDiffuse) {
    this.mapDiffuse = mapDiffuse;
  }

  public String getBlending() {
    return blending;
  }

  public void setBlending(String blending) {
    this.blending = blending;
  }

  public Boolean getDepthTest() {
    return depthTest;
  }

  public void setDepthTest(Boolean depthTest) {
    this.depthTest = depthTest;
  }

  public Boolean getDepthWrite() {
    return depthWrite;
  }

  public void setDepthWrite(Boolean depthWrite) {
    this.depthWrite = depthWrite;
  }

  public String getMapDiffuseWrap() {
    return mapDiffuseWrap;
  }

  public void setMapDiffuseWrap(String mapDiffuseWrap) {
    this.mapDiffuseWrap = mapDiffuseWrap;
  }

  public String getShading() {
    return shading;
  }

  public void setShading(String shading) {
    this.shading = shading;
  }

  public Integer getSpecularCoef() {
    return specularCoef;
  }

  public void setSpecularCoef(Integer specularCoef) {
    this.specularCoef = specularCoef;
  }

  public Float getTransparency() {
    return transparency;
  }

  public void setTransparency(Float transparency) {
    this.transparency = transparency;
  }

  public Boolean getVertexColors() {
    return vertexColors;
  }

  public void setVertexColors(Boolean vertexColors) {
    this.vertexColors = vertexColors;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    Material other = (Material) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
