package game.models;

import java.io.Serializable;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.Mesh;

/**
 * The BaseModel extends from Ador3D Mesh and is our base model for enemys,
 * towers and more.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class BaseModel<T extends Serializable> extends Mesh implements Serializable {
  public static final ReadOnlyVector3 up = new Vector3(0, 1, 0);

  private Long id;
  private Long dbId;
  private T entity;
  protected double movespeed = 30;

  public BaseModel() {
    // empty
  }

  public BaseModel(Long id, Long dbId, T entity) {
    super();
    this.id = id;
    this.dbId = dbId;
    this.entity = entity;
  }

  public void rotateTo(models.entity.game.Vector3 position, Double delta) {
    rotateTo(position.getArdorVector().clone(), delta);
  }

  public void rotateTo(Vector3 position, Double delta) {
    position.setY(0D);
    Quaternion qStart = new Quaternion();
    qStart.fromRotationMatrix(getRotation().clone());
    lookAt(position);
    Quaternion qEnd = new Quaternion();
    qEnd.fromRotationMatrix(getRotation());
    Quaternion qFinal = new Quaternion();
    qStart.slerp(qEnd, delta * (movespeed / 10), qFinal);
    setRotation(qFinal);
  }

  public void lookAt(final ReadOnlyVector3 vector) {
    final Vector3 x = Vector3.fetchTempInstance();
    final Vector3 y = Vector3.fetchTempInstance();
    final Vector3 z = Vector3.fetchTempInstance();

    vector.subtract(this.getTranslation(), z);
    z.normalizeLocal();
    if (z.length() == 0) {
      z.setZ(1);
    }

    up.cross(z, x);
    x.normalizeLocal();
    if (x.length() == 0) {
      z.setX(z.getX() + 0.0001);
      up.cross(z, x);
      x.normalizeLocal();
    }

    z.cross(x, y);

    Matrix3 m = new Matrix3();
    m.fromAxes(x, y, z);

    this.setRotation(m);
    Vector3.releaseTempInstance(x);
    Vector3.releaseTempInstance(y);
    Vector3.releaseTempInstance(z);
  }

  public void move(double delta, int column) {
    final Vector3 loc = new Vector3();
    loc.addLocal(this.getRotation().getColumn(column, null));
    loc.normalizeLocal().multiplyLocal(delta * movespeed).addLocal(this.getTranslation());
    this.setTranslation(loc);
  }

  // GETTER & SETTER //

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

  public T getEntity() {
    return entity;
  }

  public void setEntity(T entity) {
    this.entity = entity;
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
