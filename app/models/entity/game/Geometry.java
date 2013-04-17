package models.entity.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import models.entity.BaseModel;


@Entity
public class Geometry extends BaseModel {
  private static final long serialVersionUID = 5730315776315409881L;

  private static final Finder<Long, Geometry> finder = new Finder<Long, Geometry>(Long.class, Geometry.class);

  @Id
  private Long id;

  @Lob
  private String vertices;
  
  @Lob
  private String faces;

  @Lob
  private String materials;

  @Lob
  private String morphTargets;

  @Lob
  private String morphColors;

  @Lob
  private String normals;

  @Lob
  private String colors;

  @Lob
  private String usv;

  private Double scale;

  @Enumerated(EnumType.STRING)
  private GeometryType type;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private GeoMetaData metadata;

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

  public String getMaterials() {
    return materials;
  }

  public void setMaterials(String materials) {
    this.materials = materials;
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

  public String getUsv() {
    return usv;
  }

  public void setUsv(String usv) {
    this.usv = usv;
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

  public static Finder<Long, Geometry> getFinder() {
    return finder;
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
    return "Geometry [id=" + id + ", vertices=" + vertices + ", faces=" + faces + ", materials=" + materials + ", morphTargets=" + morphTargets
        + ", morphColors=" + morphColors + ", normals=" + normals + ", colors=" + colors + ", usv=" + usv + ", scale=" + scale + ", type=" + type + "]";
  }
}
