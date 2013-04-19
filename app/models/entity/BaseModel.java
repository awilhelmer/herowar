package models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import play.data.format.Formats;

/**
 * The BaseModel contains creation date, update date and version of the model.
 * 
 * @author Sebastian Sachtleben
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseModel implements Serializable {

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  protected Date cdate;

  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  protected Date udate;

  @Version
  protected Long version;

  /**
   * Default constructor set creation and update date.
   */
  public BaseModel() {
    this.cdate = new Date();
    this.udate = new Date();
  }

  // GETTER & SETTER //

  public Date getCdate() {
    return cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

  public Date getUdate() {
    return udate;
  }

  public void setUdate(Date udate) {
    this.udate = udate;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
