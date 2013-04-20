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

  @Column(precision = 1)
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
  @OneToOne(mappedBy = "metadata", cascade = CascadeType.REFRESH)
  private Geometry geometry;

  public GeoMetaData(Long id, Float formatVersion, String sourceFile, String generatedBy, Long vertices, Long faces, Long normals, Long colors, Long uvs, Long materials) {
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
    this(null, 3.1f, sourceFile, "WorldEditor", vertices, faces, normals, colors, uvs, materials);
  }
  
  public GeoMetaData() {
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
  public String toString() {
    return "GeoMetaData [id=" + id + ", formatVersion=" + formatVersion + ", sourceFile=" + sourceFile + ", generatedBy=" + generatedBy + ", vertices="
        + vertices + ", faces=" + faces + ", normals=" + normals + ", Colors=" + colors + ", uvs=" + uvs + ", materials=" + materials + "]";
  }
}