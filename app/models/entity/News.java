package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import controllers.Application;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class News extends BaseModel {

  @Id
  private Long id;

  private String headline;

  @Column(length = 2000)
  private String text;

  @ManyToOne
  private User author;

  public static void merge(News news, News news2) {
    news.setHeadline(news2.getHeadline());
    news.setText(news2.getText());
    news.save();
  }

  public static void create(String headline, String text) {
    create(headline, text, Application.getLocalUser());
  }

  public static void create(String headline, String text, User author) {
    final News news = new News();
    news.setHeadline(headline);
    news.setText(text);
    news.setAuthor(author);
    news.save();
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }
}
