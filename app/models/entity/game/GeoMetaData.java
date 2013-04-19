package models.entity.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.entity.BaseModel;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class GeoMetaData extends BaseModel {
  private static final long serialVersionUID = -9218495880941499621L;
  
  @Id
  private Long id;
  
  @Column(precision = 1)
  private Float formatVersion;
  private String sourceFile;
  private String generatedBy;
  private Long vertices;
  private Long faces;
  private Long normals;
  private Long colors;
  private Long usvs;
  private Long materials;

  @JsonIgnore
  @OneToOne(mappedBy = "metadata")
  private Geometry geometry;

  public GeoMetaData() { }

  public GeoMetaData(Float formatVersion, String sourceFile, String generatedBy, Long vertices, Long faces, Long normals, Long colors, Long usvs, Long materials) {
    this.formatVersion = formatVersion;
    this.sourceFile = sourceFile;
    this.generatedBy = generatedBy;
    this.vertices = vertices;
    this.faces = faces;
    this.normals = normals;
    this.colors = colors;
    this.usvs = usvs;
    this.materials = materials;
  }
  
  public GeoMetaData(String sourceFile, Long vertices, Long faces, Long normals, Long colors, Long usvs, Long materials) {
    this(3.1f, sourceFile, "WorldEditor", vertices, faces, normals, colors, usvs, materials);
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

  public Long getUsvs() {
    return usvs;
  }

  public void setUsvs(Long usvs) {
    this.usvs = usvs;
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
        + vertices + ", faces=" + faces + ", normals=" + normals + ", Colors=" + colors + ", usvs=" + usvs + ", materials=" + materials + "]";
  }
}