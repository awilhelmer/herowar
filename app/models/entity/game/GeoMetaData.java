package models.entity.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class GeoMetaData implements Serializable {
  private static final long serialVersionUID = -9218495880941499621L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(scale = 1)
  private Float formatVersion;
  private String sourceFile;
  private String generatedBy;
  private Long vertices;
  private Long faces;
  private Long normals;
  private Long colors;
  private Long uvs;
  private Long materials;

  @JsonIgnore
  @OneToOne(mappedBy = "metadata")
  private Geometry geometry;

  public GeoMetaData(Long id, Float formatVersion, String sourceFile, String generatedBy, Long vertices, Long faces, Long normals, Long colors, Long uvs,
      Long materials) {
    this.id = id;
    this.formatVersion = formatVersion;
    this.sourceFile = sourceFile;
    this.generatedBy = generatedBy;
    this.vertices = vertices;
    this.faces = faces;
    this.normals = normals;
    this.colors = colors;
    this.uvs = uvs;
    this.materials = materials;
  }

  public GeoMetaData(Float formatVersion, String sourceFile, String generatedBy, Long vertices, Long faces, Long normals, Long colors, Long uvs, Long materials) {
    this(null, formatVersion, sourceFile, generatedBy, vertices, faces, normals, colors, uvs, materials);
  }

  public GeoMetaData(String sourceFile, Long vertices, Long faces, Long normals, Long colors, Long uvs, Long materials) {
    this(null, 3.1F, sourceFile, "WorldEditor", vertices, faces, normals, colors, uvs, materials);
  }

  public GeoMetaData() {
    formatVersion = 3.1F;
    sourceFile = "";
    generatedBy = "";
    vertices = 0L;
    faces = 0L;
    normals = 0L;
    colors = 0L;
    uvs = 0L;
    materials = 0L;
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getFormatVersion() {
    return formatVersion;
  }

  public void setFormatVersion(Float formatVersion) {
    this.formatVersion = formatVersion;
  }

  public String getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  public String getGeneratedBy() {
    return generatedBy;
  }

  public void setGeneratedBy(String generatedBy) {
    this.generatedBy = generatedBy;
  }

  public Long getVertices() {
    return vertices;
  }

  public void setVertices(Long vertices) {
    this.vertices = vertices;
  }

  public Long getFaces() {
    return faces;
  }

  public void setFaces(Long faces) {
    this.faces = faces;
  }

  public Long getNormals() {
    return normals;
  }

  public void setNormals(Long normals) {
    this.normals = normals;
  }

  public Long getColors() {
    return colors;
  }

  public void setColors(Long colors) {
    this.colors = colors;
  }

  public Long getUvs() {
    return uvs;
  }

  public void setUvs(Long uvs) {
    this.uvs = uvs;
  }

  public Long getMaterials() {
    return materials;
  }

  public void setMaterials(Long materials) {
    this.materials = materials;
  }

  public Geometry getGeometry() {
    return geometry;
  }

  public void setGeometry(Geometry geometry) {
    this.geometry = geometry;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((colors == null) ? 0 : colors.hashCode());
    result = prime * result + ((faces == null) ? 0 : faces.hashCode());
    result = prime * result + ((formatVersion == null) ? 0 : formatVersion.hashCode());
    result = prime * result + ((generatedBy == null) ? 0 : generatedBy.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((materials == null) ? 0 : materials.hashCode());
    result = prime * result + ((normals == null) ? 0 : normals.hashCode());
    result = prime * result + ((sourceFile == null) ? 0 : sourceFile.hashCode());
    result = prime * result + ((uvs == null) ? 0 : uvs.hashCode());
    result = prime * result + ((vertices == null) ? 0 : vertices.hashCode());
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
    GeoMetaData other = (GeoMetaData) obj;
    if (colors == null) {
      if (other.colors != null)
        return false;
    } else if (!colors.equals(other.colors))
      return false;
    if (faces == null) {
      if (other.faces != null)
        return false;
    } else if (!faces.equals(other.faces))
      return false;
    if (formatVersion == null) {
      if (other.formatVersion != null)
        return false;
    } else if (!formatVersion.equals(other.formatVersion))
      return false;
    if (generatedBy == null) {
      if (other.generatedBy != null)
        return false;
    } else if (!generatedBy.equals(other.generatedBy))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (materials == null) {
      if (other.materials != null)
        return false;
    } else if (!materials.equals(other.materials))
      return false;
    if (normals == null) {
      if (other.normals != null)
        return false;
    } else if (!normals.equals(other.normals))
      return false;
    if (sourceFile == null) {
      if (other.sourceFile != null)
        return false;
    } else if (!sourceFile.equals(other.sourceFile))
      return false;
    if (uvs == null) {
      if (other.uvs != null)
        return false;
    } else if (!uvs.equals(other.uvs))
      return false;
    if (vertices == null) {
      if (other.vertices != null)
        return false;
    } else if (!vertices.equals(other.vertices))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "GeoMetaData [id=" + id + ", formatVersion=" + formatVersion + ", sourceFile=" + sourceFile + ", generatedBy=" + generatedBy + ", vertices="
        + vertices + ", faces=" + faces + ", normals=" + normals + ", Colors=" + colors + ", uvs=" + uvs + ", materials=" + materials + "]";
  }
}