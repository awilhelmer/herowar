package dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import controllers.Application;
import play.Logger;
import play.db.jpa.Transactional;
import providers.FormSignup;
import providers.SignupUsernamePasswordAuthUser;
import models.entity.LinkedAccount;
import models.entity.News;
import models.entity.SecurityRole;
import models.entity.TokenAction;
import models.entity.User;
import models.entity.UserPermission;
import models.entity.TokenAction.Type;
import models.entity.game.Map;

public class MainDao {

  @Transactional
  public static LinkedAccount findByProviderKey(final User user, String key) {
    return getFinder().where().eq("user", user).eq("providerKey", key).findUnique();
  }

  @Transactional
  public static void merge(News news, News news2) {
    news.setHeadline(news2.getHeadline());
    news.setText(news2.getText());

  }

  @Transactional
  public static void create(String headline, String text) {
    create(headline, text, Application.getLocalUser());
  }

  @Transactional
  public static void create(String headline, String text, User author) {
    final News news = new News();
    news.setHeadline(headline);
    news.setText(text);
    news.setAuthor(author);
  }

  @Transactional
  public static TokenAction findByToken(final String token, final Type type) {
    return find.where().eq("token", token).eq("type", type).findUnique();
  }



  @Transactional
  public static TokenAction create(final Type type, final String token, final User targetUser) {
    final TokenAction ua = new TokenAction();
    ua.targetUser = targetUser;
    ua.token = token;
    ua.type = type;
    final Date created = new Date();
    ua.created = created;
    ua.expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
    ua.save();
    return ua;
  }









  public static int getNewsCount() {
    // TODO Auto-generated method stub
    return 0;
  }


}
