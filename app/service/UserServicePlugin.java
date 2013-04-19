package service;

import models.entity.User;
import play.Application;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;

import dao.MainDao;


public class UserServicePlugin extends com.feth.play.module.pa.service.UserServicePlugin {

  public UserServicePlugin(final Application app) {
    super(app);
  }

  @Override
  public Long save(final AuthUser authUser) {
    final boolean isLinked = MainDao.existsByAuthUserIdentity(authUser);
    if (!isLinked) {
      return MainDao.create(authUser).getId();
    } else {
      // we have this user already, so return null
      return null;
    }
  }

  @Override
  public Long getLocalIdentity(final AuthUserIdentity identity) {
    // For production: Caching might be a good idea here...
    // ...and dont forget to sync the cache when users get deactivated/deleted
    final User u = MainDao.findByAuthUserIdentity(identity);
    if (u != null) {
      return u.getId();
    } else {
      return null;
    }
  }

  @Override
  public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
    if (!oldUser.equals(newUser)) {
      MainDao.merge(oldUser, newUser);
    }
    return oldUser;
  }

  @Override
  public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
    MainDao.addLinkedAccount(oldUser, newUser);
    return newUser;
  }

  @Override
  public AuthUser update(final AuthUser knownUser) {
    // User logged in again, bump last login date
    MainDao.setLastLoginDate(knownUser);
    return knownUser;
  }

}
