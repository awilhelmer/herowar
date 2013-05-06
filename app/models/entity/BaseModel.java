package models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

  @Temporal(TemporalType.TIMESTAMP)
  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  protected Date cdate;

  @Temporal(TemporalType.TIMESTAMP)
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cdate == null) ? 0 : cdate.hashCode());
    result = prime * result + ((udate == null) ? 0 : udate.hashCode());
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BaseModel other = (BaseModel) obj;
    if (cdate == null) {
      if (other.cdate != null)
        return false;
    } else if (!cdate.equals(other.cdate))
      return false;
    if (udate == null) {
      if (other.udate != null)
        return false;
    } else if (!udate.equals(other.udate))
      return false;
    if (version == null) {
      if (other.version != null)
        return false;
    } else if (!version.equals(other.version))
      return false;
    return true;
  }

}
