package models.entity.game;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.entity.BaseModel;

@Entity
public class GeoMetaData extends BaseModel {
  private static final long serialVersionUID = -9218495880941499621L;
  @Id
  private Long id;
  private String formatVersion;
  private String sourceFile;
  private String generatedBy;
  private Long vertices;
  private Long faces;
  private Long normal;
  private Long Colors;
  private Long usvs;
  private Long materials;

  @OneToOne(mappedBy = "metadata")
  private Geometry geometry;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFormatVersion() {
    return formatVersion;
  }

  public void setFormatVersion(String formatVersion) {
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

  public Long getNormal() {
    return normal;
  }

  public void setNormal(Long normal) {
    this.normal = normal;
  }

  public Long getColors() {
    return Colors;
  }

  public void setColors(Long colors) {
    Colors = colors;
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

}