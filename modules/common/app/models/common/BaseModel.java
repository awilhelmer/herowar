package models.common;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import play.db.ebean.Model;

/**
 * The BaseModel contains creation date, update date and version of the model.
 * 
 * @author Sebastian Sachtleben
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseModel extends Model {

  protected Date cdate;
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
