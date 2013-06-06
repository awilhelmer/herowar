package models.entity.game;

import game.json.JsonFieldName;
import game.json.MaterialDeserializer;
import game.json.StringArray;
import game.json.StringArray.ArrayType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * The mesh materials for the game models.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "material", uniqueConstraints = @UniqueConstraint(columnNames = "dbgname"))
@JsonDeserialize(using = MaterialDeserializer.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@SuppressWarnings("serial")
public class Material implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(length = 100)
  private String name;

  private Integer sortIndex;

  private String color = "#ffffff";

  // They are for model imports
  @JsonFieldName(name = "DbgColor")
  private Integer dbgColor;
  @JsonFieldName(name = "DbgIndex")
  private Integer dbgIndex;
  @JsonFieldName(name = "DbgName")
  @Column(name = "dbgname")
  private String dbgName;

  private String blending = "NormalBlending";
  private String shading = "SmoothShading";

  private Boolean depthTest = Boolean.FALSE;
  private Boolean depthWrite = Boolean.FALSE;

  private Boolean transparent = Boolean.FALSE;
  private Boolean vertexColors = Boolean.FALSE;

  @StringArray(type = ArrayType.DOUBLE)
  private String colorDiffuse = "[1.0, 1.0, 1.0]";
  @StringArray(type = ArrayType.DOUBLE)
  private String colorSpecular = "[0.0, 0.0, 0.0]";
  @StringArray(type = ArrayType.DOUBLE)
  private String colorAmbient = "[1.0, 1.0, 1.0]";

  @Column(scale = 3)
  private Float transparency = 1.0F;
  private Integer specularCoef = 30;

  @Column(precision = 2)
  private Float opacity = 1.0F;

  private String mapDiffuse = null;
  private String mapLight = null;
  private String mapBump = null;
  private String mapNormal = null;
  private String mapSpecular = null;

  @StringArray(type = ArrayType.STRING)
  private String mapDiffuseWrap = null;
  @StringArray(type = ArrayType.STRING)
  private String mapLightWrap = null;
  @StringArray(type = ArrayType.STRING)
  private String mapBumpWrap = null;
  @StringArray(type = ArrayType.STRING)
  private String mapNormalWrap = null;
  @StringArray(type = ArrayType.STRING)
  private String mapSpecularWrap = null;

  @Column(scale = 3)
  private Float mapBumpScale = 1.0F;

  @Lob
  @StringArray(type = ArrayType.OBJECT)
  private String attributes = null;
  @Lob
  @StringArray(type = ArrayType.OBJECT)
  private String uniforms = null;

  @Lob
  @StringArray(type = ArrayType.SHADER)
  private String vertexShader = null;

  @Lob
  @StringArray(type = ArrayType.SHADER)
  private String fragmentShader = null;

  @Transient
  private Long materialId;

  @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
  private Texture texture;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "allMaterials")
  @JsonIgnore
  private Set<Map> maps = new HashSet<Map>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "id.material")
  @JsonIgnore
  private Set<GeoMaterial> geoMaterials = new HashSet<GeoMaterial>();

  // / GETTER && SETTER ///

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSortIndex() {
    return sortIndex;
  }

  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Integer getDbgColor() {
    return dbgColor;
  }

  public void setDbgColor(Integer dbgColor) {
    this.dbgColor = dbgColor;
  }

  public Integer getDbgIndex() {
    return dbgIndex;
  }

  public void setDbgIndex(Integer dbgIndex) {
    this.dbgIndex = dbgIndex;
  }

  public String getDbgName() {
    return dbgName;
  }

  public void setDbgName(String dbgName) {
    this.dbgName = dbgName;
  }

  public String getBlending() {
    return blending;
  }

  public void setBlending(String blending) {
    this.blending = blending;
  }

  public String getShading() {
    return shading;
  }

  public void setShading(String shading) {
    this.shading = shading;
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

  public Boolean getTransparent() {
    return transparent;
  }

  public void setTransparent(Boolean transparent) {
    this.transparent = transparent;
  }

  public Boolean getVertexColors() {
    return vertexColors;
  }

  public void setVertexColors(Boolean vertexColors) {
    this.vertexColors = vertexColors;
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

  public String getColorAmbient() {
    return colorAmbient;
  }

  public void setColorAmbient(String colorAmbient) {
    this.colorAmbient = colorAmbient;
  }

  public Float getTransparency() {
    return transparency;
  }

  public void setTransparency(Float transparency) {
    this.transparency = transparency;
  }

  public Integer getSpecularCoef() {
    return specularCoef;
  }

  public void setSpecularCoef(Integer specularCoef) {
    this.specularCoef = specularCoef;
  }

  public Float getOpacity() {
    return opacity;
  }

  public void setOpacity(Float opacity) {
    this.opacity = opacity;
  }

  public String getMapDiffuse() {
    return mapDiffuse;
  }

  public void setMapDiffuse(String mapDiffuse) {
    this.mapDiffuse = mapDiffuse;
  }

  public String getMapLight() {
    return mapLight;
  }

  public void setMapLight(String mapLight) {
    this.mapLight = mapLight;
  }

  public String getMapBump() {
    return mapBump;
  }

  public void setMapBump(String mapBump) {
    this.mapBump = mapBump;
  }

  public String getMapNormal() {
    return mapNormal;
  }

  public void setMapNormal(String mapNormal) {
    this.mapNormal = mapNormal;
  }

  public String getMapSpecular() {
    return mapSpecular;
  }

  public void setMapSpecular(String mapSpecular) {
    this.mapSpecular = mapSpecular;
  }

  public String getMapDiffuseWrap() {
    return mapDiffuseWrap;
  }

  public void setMapDiffuseWrap(String mapDiffuseWrap) {
    this.mapDiffuseWrap = mapDiffuseWrap;
  }

  public String getMapLightWrap() {
    return mapLightWrap;
  }

  public void setMapLightWrap(String mapLightWrap) {
    this.mapLightWrap = mapLightWrap;
  }

  public String getMapBumpWrap() {
    return mapBumpWrap;
  }

  public void setMapBumpWrap(String mapBumpWrap) {
    this.mapBumpWrap = mapBumpWrap;
  }

  public String getMapNormalWrap() {
    return mapNormalWrap;
  }

  public void setMapNormalWrap(String mapNormalWrap) {
    this.mapNormalWrap = mapNormalWrap;
  }

  public String getMapSpecularWrap() {
    return mapSpecularWrap;
  }

  public void setMapSpecularWrap(String mapSpecularWrap) {
    this.mapSpecularWrap = mapSpecularWrap;
  }

  public Float getMapBumpScale() {
    return mapBumpScale;
  }

  public void setMapBumpScale(Float mapBumpScale) {
    this.mapBumpScale = mapBumpScale;
  }

  public String getAttributes() {
    return attributes;
  }

  public void setAttributes(String attributes) {
    this.attributes = attributes;
  }

  public String getUniforms() {
    return uniforms;
  }

  public void setUniforms(String uniforms) {
    this.uniforms = uniforms;
  }

  public String getVertexShader() {
    return vertexShader;
  }

  public void setVertexShader(String vertexShader) {
    this.vertexShader = vertexShader;
  }

  public String getFragmentShader() {
    return fragmentShader;
  }

  public void setFragmentShader(String fragmentShader) {
    this.fragmentShader = fragmentShader;
  }

  public Long getMaterialId() {
    return materialId;
  }

  public void setMaterialId(Long materialId) {
    this.materialId = materialId;
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public Set<Map> getMaps() {
    return maps;
  }

  public void setMaps(Set<Map> maps) {
    this.maps = maps;
  }

  public Set<GeoMaterial> getGeoMaterials() {
    return geoMaterials;
  }

  public void setGeoMaterials(Set<GeoMaterial> geoMaterials) {
    this.geoMaterials = geoMaterials;
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
