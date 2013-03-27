package models.common;

import javax.persistence.Entity;

import play.db.ebean.Model;

@Entity
@SuppressWarnings("serial")
public class User extends Model {

    public String username;
    public String password;

}
