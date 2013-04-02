package models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class News extends BaseModel {

  @Id
  private Long id;
  
  private String headline;
  private String text;
  
  @ManyToOne
  private User author;

  public static void merge(News news, News news2) {
    news.setHeadline(news2.getHeadline());
    news.setText(news2.getText());
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
