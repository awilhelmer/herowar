package dao;

import play.db.jpa.JPA;
import models.entity.News;
import models.entity.User;
import controllers.Application;

public class NewsDAO extends BaseDAO<Long, News> {

  private NewsDAO() {
    super(Long.class, News.class);
  }

  private static final NewsDAO instance = new NewsDAO();

  public static void merge(News news, News news2) {
    news.setHeadline(news2.getHeadline());
    news.setText(news2.getText());
  }

  public static News create(String headline, String text) {
    return create(headline, text, Application.getLocalUser());
  }

  public static News create(String headline, String text, User author) {
    final News news = new News();
    news.setHeadline(headline);
    news.setText(text);
    news.setAuthor(author);
    JPA.em().persist(news);
    return news;
  }

  public static long getNewsCount() {
    return instance.getBaseCount();
  }

}
