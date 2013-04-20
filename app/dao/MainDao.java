package dao;

import models.entity.News;
import models.entity.User;
import play.db.jpa.Transactional;
import controllers.Application;

public class MainDao {

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

  public static int getNewsCount() {
    // TODO Auto-generated method stub
    return 0;
  }

}
