package game.json;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import models.entity.game.Vector3;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonSerializer;
import org.hibernate.Hibernate;

import play.Logger;

/**
 * The BaseSerializer provides several methods which helps to serialize a pojo
 * properly.
 * 
 * @author Sebastian Sachtleben
 * 
 * @param <T>
 *          The class to serialize
 */
public abstract class BaseSerializer<T> extends JsonSerializer<T> {
  private static final Logger.ALogger log = Logger.of(BaseSerializer.class);

  private DoubleConverter doubleConverter = new DoubleConverter();
  private StringConverter stringConverter = new StringConverter();
  private ArrayConverter arrayDoubleConverter = new ArrayConverter(double[].class, doubleConverter);
  private ArrayConverter arrayStringConverter = new ArrayConverter(String[].class, stringConverter);

  protected void writeAll(JsonGenerator jgen, T obj, Class<?>... clazz) throws JsonGenerationException, IOException {
    List<Class<?>> classes = Arrays.asList(clazz);

    jgen.writeStartObject();
    writeObject(jgen, obj, classes);
    jgen.writeEndObject();
  }

  @SuppressWarnings("unchecked")
  private void writeObject(JsonGenerator jgen, Object obj, List<Class<?>> classes) throws JsonGenerationException, IOException {
    // PropertyDescriptor[] properties =
    // PropertyUtils.getPropertyDescriptors(obj);
    Class<?> clazz = obj.getClass();

    if (Hibernate.getClass(obj) != null) {
      clazz = Hibernate.getClass(obj);
    }
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      Class<?> type = field.getType();
      try {
        if (type != null && !type.isAnnotation() && !Modifier.isStatic(field.getModifiers())) {
          Object value = PropertyUtils.getProperty(obj, field.getName());

          if (value != null && !field.isAnnotationPresent(JsonIgnore.class)) {
            if (type.isAssignableFrom(String.class)) {
              if (field != null && field.isAnnotationPresent(StringArray.class)) {
                StringArray anno = field.getAnnotation(StringArray.class);
                int dimension = anno.dimensions();
                switch (anno.type()) {
                case INTEGER:
                case DOUBLE:
                  if (dimension == 1) {
                    writeStringAsDoubleArray(jgen, field.getName(), value.toString());
                  } else {
                    writeStringAsDoubleMultiArray(jgen, field.getName(), value.toString());
                  }
                  break;
                case STRING:
                  writeStringAsStringArray(jgen, field.getName(), value.toString());
                  if (dimension > 1) {
                    log.warn(String.format("Field <%s> in Bean <%s>: MultiStringArray in StringArray not supported!", field.getName(), clazz.getSimpleName()));
                  }
                  break;
                default:
                  log.warn(String.format("Field <%s> in Bean <%s>: StringArray Type <%s> not supported!", field.getName(), clazz.getSimpleName(), anno.type()));
                  break;
                }

              } else {
                jgen.writeStringField(field.getName(), value.toString());
              }

            } else if (type.isAssignableFrom(Double.class)) {
              jgen.writeNumberField(field.getName(), (Double) value);
            } else if (type.isAssignableFrom(Float.class)) {
              jgen.writeNumberField(field.getName(), (Float) value);
            } else if (type.isAssignableFrom(Integer.class)) {
              jgen.writeNumberField(field.getName(), (Integer) value);
            } else if (type.isAssignableFrom(Long.class)) {
              jgen.writeNumberField(field.getName(), (Long) value);
            } else if (type.isAssignableFrom(Short.class)) {
              jgen.writeNumberField(field.getName(), (Short) value);
            } else if (type.isAssignableFrom(Boolean.class)) {
              jgen.writeBooleanField(field.getName(), (Boolean) value);
            } else if (type.isAssignableFrom(Vector3.class)) {
              Vector3 vec = (Vector3) value;
              jgen.writeObjectField(field.getName(), vec);
            } else if (type.isAssignableFrom(List.class) || type.isAssignableFrom(Set.class)) {
              ParameterizedType pt = (ParameterizedType) field.getGenericType();
              Class<?> genericType = (Class<?>) pt.getActualTypeArguments()[0];
              if (classes.contains(genericType)) {
                jgen.writeArrayFieldStart(field.getName());
                Collection<Object> col = (Collection<Object>) value;
                for (Object elem : col) {
                  jgen.writeStartObject();
                  writeObject(jgen, elem, classes);
                  jgen.writeEndObject();
                }
                jgen.writeEndArray();
              }

            } else if (field.isEnumConstant()) {
              // not supported yet
            }

            else if (classes.contains(type)) {
              jgen.writeObjectFieldStart(field.getName());
              writeObject(jgen, value, classes);
              jgen.writeEndObject();
            } else {
              log.warn(String.format("Field <%s> ignored in Bean <%s>", field.getName(), clazz.getSimpleName()));
            }
          } else {
            jgen.writeNullField(field.getName());
          }

        }
      } catch (Exception e) {
        log.error("", e);
      }
    }

  }

  protected void writeStringAsDoubleMultiArray(JsonGenerator jgen, String name, String value) throws JsonGenerationException, IOException {
    if (value != null && !"".equals(value)) {
      if (value.startsWith("[[")) {
        value = value.substring(2);
        value = value.substring(0, value.length() - 2);
      } else {
        value = value.substring(1);
        value = value.substring(0, value.length() - 1);
      }
      jgen.writeArrayFieldStart(name);
      jgen.writeStartArray();
      if (value != null && !"".equals(value)) {
        double[] values = (double[]) arrayDoubleConverter.convert(double[].class, value);
        for (int i = 0; i < values.length; i++) {
          jgen.writeNumber(values[i]);
        }
      }
      jgen.writeEndArray();
      jgen.writeEndArray();
    }
  }

  protected void writeStringAsDoubleArray(JsonGenerator jgen, String name, String value) throws JsonGenerationException, IOException {
    if (value != null && !"".equals(value)) {
      value = value.substring(1);
      value = value.substring(0, value.length() - 1);
      String[] parts = value.split(",");
      jgen.writeArrayFieldStart(name);
      if (value != null && !"".equals(value) && parts != null && parts.length > 0) {
        double[] values = (double[]) arrayDoubleConverter.convert(double[].class, parts);
        for (int i = 0; i < values.length; i++) {
          jgen.writeNumber(values[i]);
        }
      }
      jgen.writeEndArray();
    }
  }

  protected void writeStringAsStringArray(JsonGenerator jgen, String name, String value) throws JsonGenerationException, IOException {
    if (value != null && !"".equals(value)) {
      value = value.substring(1);
      value = value.substring(0, value.length() - 1);
      String[] parts = value.split(",");
      jgen.writeArrayFieldStart(name);
      if (value != null && !"".equals(value) && parts != null && parts.length > 0) {
        String[] values = (String[]) arrayStringConverter.convert(String[].class, parts);
        for (int i = 0; i < values.length; i++) {
          jgen.writeString(values[i]);
        }
      }
      jgen.writeEndArray();
    }
  }

  protected void writeStringField(JsonGenerator jgen, String name, String value) throws JsonGenerationException, IOException {
    if (value != null) {
      jgen.writeStringField(name, value);
    } else {
      jgen.writeNullField(name);
    }
  }

  protected void writeBooleanField(JsonGenerator jgen, String name, Boolean value) throws JsonGenerationException, IOException {
    if (value != null) {
      jgen.writeBooleanField(name, value);
    } else {
      jgen.writeNullField(name);
    }
  }

  protected void writeNumberField(JsonGenerator jgen, String name, Long value) throws JsonGenerationException, IOException {
    if (value != null) {
      jgen.writeNumberField(name, value);
    } else {
      jgen.writeNullField(name);
    }
  }

  protected void writeNumberField(JsonGenerator jgen, String name, Integer value) throws JsonGenerationException, IOException {
    if (value != null) {
      jgen.writeNumberField(name, value);
    } else {
      jgen.writeNullField(name);
    }
  }

  protected void writeNumberField(JsonGenerator jgen, String name, Double value) throws JsonGenerationException, IOException {
    if (value != null) {
      jgen.writeNumberField(name, value);
    } else {
      jgen.writeNullField(name);
    }
  }

  protected void writeNumberField(JsonGenerator jgen, String name, Float value) throws JsonGenerationException, IOException {
    if (value != null) {
      jgen.writeNumberField(name, value);
    } else {
      jgen.writeNullField(name);
    }
  }
}
