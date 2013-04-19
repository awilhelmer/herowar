package models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import be.objectify.deadbolt.core.models.Permission;

@Entity
@SuppressWarnings("serial")
public class UserPermission implements Permission, Serializable {

  @Id
  private Long id;

  private String value;

  public String getValue() {
    return value;
  }



  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
