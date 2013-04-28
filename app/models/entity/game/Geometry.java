package models.entity.game;

import game.json.GeometryDeserializer;
import game.json.GeometrySerializer;
import game.json.StringArray;
import game.json.StringArray.ArrayType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import dao.game.GeoMatId;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonDeserialize(using = GeometryDeserializer.class)
@JsonSerialize(using = GeometrySerializer.class)
@SuppressWarnings("serial")
public class Geometry implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @StringArray(type = ArrayType.DOUBLE)
  private String vertices;

  @Lob
  @StringArray(type = ArrayType.DOUBLE)
  private String faces;

  @Lob
  @StringArray(type = ArrayType.DOUBLE)
  private String morphTargets;

  @Lob
  @StringArray(type = ArrayType.DOUBLE)
  private String morphColors;

  @Lob
  @StringArray(type = ArrayType.DOUBLE)
  private String normals;

  @Lob
  @StringArray(type = ArrayType.DOUBLE)
  private String colors;

  @Lob
  @StringArray(type = ArrayType.DOUBLE, dimensions = 2)
  private String uvs;

  @Column(scale = 6)
  private Double scale;

  @Enumerated(EnumType.STRING)
  private GeometryType type;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private GeoMetaData metadata;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "geometry", optional = true)
  @JsonIgnore
  private Terrain terrain;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "geometry")
  @JsonIgnore
  private Set<Mesh> meshes;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "id.geometry")
  @JsonIgnore
  private Set<GeoMaterial> geoMaterials;

  @Transient
  private List<GeoMatId> matIdMapper;

  @Transient
  private List<Material> materials;

  public Geometry() {
    vertices = "[]";
    faces = "[]";
    morphTargets = "[]";
    morphColors = "[]";
    normals = "[]";
    colors = "[]";
    uvs = "[[]]";
    scale = 1D;
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getVertices() {
    return vertices;
  }

  public void setVertices(String vertices) {
    this.vertices = vertices;
  }

  public String getFaces() {
    return faces;
  }

  public void setFaces(String faces) {
    this.faces = faces;
  }

  public String getMorphTargets() {
    return morphTargets;
  }

  public void setMorphTargets(String morphTargets) {
    this.morphTargets = morphTargets;
  }

  public String getMorphColors() {
    return morphColors;
  }

  public void setMorphColors(String morphColors) {
    this.morphColors = morphColors;
  }

  public String getNormals() {
    return normals;
  }

  public void setNormals(String normals) {
    this.normals = normals;
  }

  public String getColors() {
    return colors;
  }

  public void setColors(String colors) {
    this.colors = colors;
  }

  public String getUvs() {
    return uvs;
  }

  public void setUvs(String uvs) {
    this.uvs = uvs;
  }

  public Double getScale() {
    return scale;
  }

  public void setScale(Double scale) {
    this.scale = scale;
  }

  public GeoMetaData getMetadata() {
    return metadata;
  }

  public void setMetadata(GeoMetaData metadata) {
    this.metadata = metadata;
  }

  public GeometryType getType() {
    return type;
  }

  public void setType(GeometryType type) {
    this.type = type;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public void setTerrain(Terrain terrain) {
    this.terrain = terrain;
  }

  public List<GeoMatId> getMatIdMapper() {
    return matIdMapper;
  }

  public void setMatIdMapper(List<GeoMatId> matIdMapper) {
    this.matIdMapper = matIdMapper;
  }

  public Set<GeoMaterial> getGeoMaterials() {
    return geoMaterials;
  }

  public void setGeoMaterials(Set<GeoMaterial> geoMaterials) {
    this.geoMaterials = geoMaterials;
  }

  public List<Material> getMaterials() {
    return materials;
  }

  public void setMaterials(List<Material> materials) {
    this.materials = materials;
  }

  public Set<Mesh> getMeshes() {
    return meshes;
  }

  public void setMeshes(Set<Mesh> meshes) {
    this.meshes = meshes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Geometry other = (Geometry) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Geometry [id=" + id + ", vertices=" + vertices + ", faces=" + faces + ", morphTargets=" + morphTargets + ", morphColors=" + morphColors
        + ", normals=" + normals + ", colors=" + colors + ", uvs=" + uvs + ", scale=" + scale + ", type=" + type + ", metadata=" + metadata + ", terrain="
        + terrain + ", meshes=" + meshes + ", geoMaterials=" + geoMaterials + ", matIdMapper=" + matIdMapper + ", materials=" + materials + "]";
  }

}
