package game.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

/**
 * The Matrix3 class extend the ardor3d matrix and allows to create a proper
 * look at method.
 * 
 * @author Sebastian Sachtleben
 */
public class Matrix3 extends com.ardor3d.math.Matrix3 {

  public static Matrix3 lookAt(final ReadOnlyVector3 eye, final ReadOnlyVector3 target, final ReadOnlyVector3 up) {
    final Vector3 x = Vector3.fetchTempInstance();
    final Vector3 y = Vector3.fetchTempInstance();
    final Vector3 z = Vector3.fetchTempInstance();
    
    target.subtract(eye, z);
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
    
    Vector3.releaseTempInstance(x);
    Vector3.releaseTempInstance(y);
    Vector3.releaseTempInstance(z);
    
    return m;
  }
  
}
