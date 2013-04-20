package dao;

import models.entity.UserPermission;
import play.db.jpa.Transactional;

public class UserPermissionDAO extends BaseDAO<Long, UserPermission> {

  private UserPermissionDAO() {
    super(Long.class, UserPermission.class);
  }

  private static final UserPermissionDAO instance = new UserPermissionDAO();

  @Transactional
  public static UserPermission findByValue(String value) {
    return instance.getSingleByPropertyValue("value", value);
  }
}
