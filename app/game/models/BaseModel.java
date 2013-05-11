package game.models;

import java.io.Serializable;

import com.ardor3d.scenegraph.Mesh;

public class BaseModel extends Mesh implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long id;
  private Long dbId;

  public BaseModel() {

  }

  public BaseModel(Long id, Long dbId) {
    super();
    this.id = id;
    this.dbId = dbId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
    this.setName(id.toString());
  }

  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
