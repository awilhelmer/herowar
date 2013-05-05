package game.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface StringArray {

  enum ArrayType {
    INTEGER, DOUBLE, STRING, OBJECT
  }

  ArrayType type();

  int dimensions() default 1;
}
