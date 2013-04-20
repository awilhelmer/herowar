package dao;

import play.db.jpa.Transactional;
import controllers.Application;
import models.entity.News;
import models.entity.User;

public class NewsDAO extends BaseDAO<Long, News> {

  private NewsDAO() {
    super(Long.class, News.class);
  }

  private static final NewsDAO instance = new NewsDAO();

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
  public static long getNewsCount() {
    return instance.getBaseCount();
  }

}
