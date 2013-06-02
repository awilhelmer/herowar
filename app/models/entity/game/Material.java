package models.entity.game;

import game.json.JsonFieldName;
import game.json.MaterialDeserializer;
import game.json.StringArray;
import game.json.StringArray.ArrayType;

import java.io.Serializable;
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

@Entity
@Table(name = "material", uniqueConstraints = @UniqueConstraint(columnNames = "dbgname"))
@JsonDeserialize(using = MaterialDeserializer.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Material implements Serializable {
  private static final long serialVersionUID = 1651915135235L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  private String color;
  // They are for model imports
  @JsonFieldName(name = "DbgColor")
  private Integer dbgColor;
  @JsonFieldName(name = "DbgIndex")
  private Integer dbgIndex;
  @JsonFieldName(name = "DbgName")
  @Column(name = "dbgname")
  private String dbgName;

  private String blending;
  private Boolean depthTest;
  private Boolean depthWrite;

  private Integer sortIndex;

  @StringArray(type = ArrayType.STRING)
  private String mapDiffuseWrap;
  private String shading;
  private Integer specularCoef;
  @Column(scale = 3)
  private Float transparency;
  private Boolean vertexColors;
  private Boolean transparent;

  @StringArray(type = ArrayType.DOUBLE)
  private String colorAmbient;
  @StringArray(type = ArrayType.DOUBLE)
  private String colorDiffuse;
  @StringArray(type = ArrayType.DOUBLE)
  private String colorSpecular;
  private String mapDiffuse;

  @Lob
  @StringArray(type = ArrayType.OBJECT)
  private String attributes;
  @Lob
  @StringArray(type = ArrayType.OBJECT)
  private String uniforms;

  @Lob
  @StringArray(type = ArrayType.SHADER)
  private String vertexShader;

  @Lob
  @StringArray(type = ArrayType.SHADER)
  private String fragmentShader;
  @Transient
  private Long materialId;

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

  @Column(name = "name", length = 100)
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

  public Long getMaterialId() {
    return materialId;
  }

  public void setMaterialId(Long materialId) {
    this.materialId = materialId;
  }

  public Integer getSortIndex() {
    return sortIndex;
  }

  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
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
