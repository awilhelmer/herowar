package models.entity.game;

import javax.persistence.Id;

import models.entity.BaseModel;

public class GeoMetaData extends BaseModel {
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
  
  
  
  
  
  
}