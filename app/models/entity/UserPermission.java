package models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Permission;

@Entity
@SuppressWarnings("serial")
public class UserPermission extends Model implements Permission {

  @Id
  private Long id;

  private String value;

  private static final Model.Finder<Long, UserPermission> find = new Model.Finder<Long, UserPermission>(Long.class, UserPermission.class);

  public String getValue() {
    return value;
  }

  public static UserPermission findByValue(String value) {
    return find.where().eq("value", value).findUnique();
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
