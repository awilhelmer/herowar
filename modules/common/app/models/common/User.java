package models.common;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
@SuppressWarnings("serial")
public class User extends Model {

  @Id
  public Long id;
  public String username;
  public String password;
  public String email;
  public Boolean newsletter;

}
