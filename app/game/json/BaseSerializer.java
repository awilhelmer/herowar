package game.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.entity.game.GeoMetaData;
import models.entity.game.Vector3;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang3.ArrayUtils;
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
    Field[] fields = getAllFields(clazz);

    for (Field field : fields) {
      Class<?> type = field.getType();
      try {
        if (type != null && !type.isAnnotation() && !Modifier.isStatic(field.getModifiers())) {
          Object value = PropertyUtils.getProperty(obj, field.getName());
          String fieldName = field.getName();
          if (field != null && field.isAnnotationPresent(JsonFieldName.class)) {
            JsonFieldName annoName = field.getAnnotation(JsonFieldName.class);
            fieldName = annoName.name();
          }
          if (value != null && !field.isAnnotationPresent(JsonIgnore.class)) {
            if (obj.getClass() == GeoMetaData.class && fieldName.toLowerCase().startsWith("geometry"))
              log.info("ILLEGAL CIRCLE IN GEOMETADATA");
            if (type.isAssignableFrom(String.class)) {
              if (field != null && field.isAnnotationPresent(StringArray.class)) {
                StringArray anno = field.getAnnotation(StringArray.class);
                int dimension = anno.dimensions();
                switch (anno.type()) {
                case INTEGER:
                case DOUBLE:
                  if (dimension == 1) {
                    writeStringAsDoubleArray(jgen, fieldName, value.toString());
                  } else {
                    writeStringAsDoubleMultiArray(jgen, fieldName, value.toString());
                  }
                  break;
                case STRING:
                  writeStringAsStringArray(jgen, fieldName, value.toString());
                  if (dimension > 1) {
                    log.warn(String.format("Field <%s> in Bean <%s>: MultiStringArray in StringArray not supported!", fieldName, clazz.getSimpleName()));
                  }
                  break;
                case OBJECT:
                  writeStringAsObjectArray(jgen, fieldName, value.toString());
                  break;
                case SHADER:
                  jgen.writeStringField(fieldName, value.toString());
                  break;
                default:
                  log.warn(String.format("Field <%s> in Bean <%s>: StringArray Type <%s> not supported!", fieldName, clazz.getSimpleName(), anno.type()));
                  break;
                }

              } else {
                jgen.writeStringField(fieldName, value.toString());
              }

            } else if (type.isAssignableFrom(Double.class)) {
              jgen.writeNumberField(fieldName, (Double) value);
            } else if (type.isAssignableFrom(Float.class)) {
              jgen.writeNumberField(fieldName, (Float) value);
            } else if (type.isAssignableFrom(Integer.class)) {
              jgen.writeNumberField(fieldName, (Integer) value);
            } else if (type.isAssignableFrom(Long.class)) {
              jgen.writeNumberField(fieldName, (Long) value);
            } else if (type.isAssignableFrom(Short.class)) {
              jgen.writeNumberField(fieldName, (Short) value);
            } else if (type.isAssignableFrom(Boolean.class)) {
              jgen.writeBooleanField(fieldName, (Boolean) value);
            } else if (type.isAssignableFrom(Date.class)) {
              jgen.writeNumberField(fieldName, ((Date) value).getTime());
            } else if (type.isAssignableFrom(Vector3.class)) {
              Vector3 vec = (Vector3) value;
              jgen.writeObjectField(fieldName, vec);
            } else if (type.isAssignableFrom(List.class) || type.isAssignableFrom(Set.class)) {
              ParameterizedType pt = (ParameterizedType) field.getGenericType();
              Class<?> genericType = (Class<?>) pt.getActualTypeArguments()[0];
              if (classes.contains(genericType)) {
                jgen.writeArrayFieldStart(fieldName);
                Collection<Object> col = (Collection<Object>) value;
                for (Object elem : col) {
                  jgen.writeStartObject();
                  writeObject(jgen, elem, classes);
                  jgen.writeEndObject();
                }
                jgen.writeEndArray();
              }

            } else if (type.isEnum()) {
              jgen.writeStringField(fieldName, value.toString());
            }

            else if (classes.contains(type)) {
              jgen.writeObjectFieldStart(fieldName);
              writeObject(jgen, value, classes);
              jgen.writeEndObject();
            } else {
              log.warn(String.format("Field <%s> ignored in Bean <%s>", field.getName(), clazz.getSimpleName()));
            }
          } else {
            jgen.writeNullField(fieldName);
          }

        }
      } catch (Exception e) {
        log.error("", e);
      }
    }

  }

  private Field[] getAllFields(Class<?> clazz) {
    Field[] result = clazz.getDeclaredFields();
    if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
      result = ArrayUtils.addAll(result, getAllFields(clazz.getSuperclass()));
    }

    return result;
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

  protected void writeStringAsObjectArray(JsonGenerator jgen, String name, String value) throws JsonGenerationException, IOException {
    if (value != null && !"".equals(value)) {
      value = value.substring(1);
      value = value.substring(0, value.length() - 1);
      List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
      int nextIndex = value.indexOf("}");
      while (nextIndex != -1) {
        String item = value.substring(0, nextIndex);
        while (!item.startsWith("\"")) {
          item = item.substring(1);
        }
        objects.add(parseObjectProperties(item));
        value = value.substring(nextIndex + 1);
        nextIndex = value.indexOf("}");
      }
      jgen.writeArrayFieldStart(name);
      if (objects != null && objects.size() > 0) {
        for (Map<String, String> obj : objects) {
          jgen.writeStartObject();
          for (Map.Entry<String, String> entry : obj.entrySet()) {
            if (entry.getValue().startsWith("[")) {
              writeStringAsDoubleArray(jgen, entry.getKey(), entry.getValue());
            } else {
              jgen.writeStringField(entry.getKey(), entry.getValue());
            }
          }
          jgen.writeEndObject();
        }
      }
      jgen.writeEndArray();
    }
  }

  private Map<String, String> parseObjectProperties(String object) {
    Map<String, String> properties = new HashMap<String, String>();
    int index = object.indexOf(",\"");
    boolean isLast = true;
    while (index != -1 || isLast) {
      String property = index != -1 ? object.substring(0, index) : object;
      String[] parts = property.split(":");
      if (parts.length == 2) {
        String key = parts[0].replaceAll("\"", "");
        String value = parts[1].replaceAll("\"", "");
        properties.put(key, value);
        object = object.substring(index + 2);
        if (index == -1) {
          isLast = false;
        }
        index = object.indexOf(",\"");
      }
    }
    return properties;
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
