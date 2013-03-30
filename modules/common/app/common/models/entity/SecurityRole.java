package common.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Role;

@Entity
@SuppressWarnings("serial")
public class SecurityRole extends Model implements Role {

  @Id
  private Long id;

  private String roleName;

  private static final Finder<Long, SecurityRole> finder = new Finder<Long, SecurityRole>(Long.class, SecurityRole.class);

  @Override
  public String getName() {
    return roleName;
  }

  public static SecurityRole findByRoleName(String roleName) {
    return getFinder().where().eq("roleName", roleName).findUnique();
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public static Finder<Long, SecurityRole> getFinder() {
    return finder;
  }
}
