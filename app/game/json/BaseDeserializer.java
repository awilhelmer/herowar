package game.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.entity.game.Vector3;

import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonDeserializer;

import play.Logger;

public abstract class BaseDeserializer<T> extends JsonDeserializer<T> {
  private static final Logger.ALogger log = Logger.of(BaseSerializer.class);

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public T parseObject(JsonNode node, Class<?>... parseClasses) {
    T result = null;
    List<Class<?>> classes = Arrays.asList(parseClasses);
    try {
      result = (T) ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
      parseAll(result, node, classes);

    } catch (Exception e) {
      log.error("", e);
    }
    return result;

  }

  @SuppressWarnings("unchecked")
  protected void parseAll(Object result, JsonNode node, List<Class<?>> classes) {
    Iterator<String> fieldIt = node.getFieldNames();
    while (fieldIt.hasNext()) {
      String field = fieldIt.next();
      JsonNode fieldNode = node.get(field);
      if (fieldNode != null && !fieldNode.isNull()) {
        try {
          field = getRealField(field, result);
          Class<?> propClass = PropertyUtils.getPropertyType(result, field);
          Object value = null;
          if (propClass != null) {
            if (propClass.isAssignableFrom(Double.class)) {
              value = fieldNode.getDoubleValue();
            } else if (propClass.isAssignableFrom(String.class)) {
              if (fieldNode.isArray()) {
                value = fieldNode.toString();
              } else {
                value = fieldNode.getTextValue();
              }
            } else if (propClass.isAssignableFrom(Float.class)) {
              value = Double.valueOf(fieldNode.getDoubleValue()).floatValue();
            } else if (propClass.isAssignableFrom(Long.class)) {
              value = fieldNode.getLongValue();
            } else if (propClass.isAssignableFrom(Integer.class)) {
              value = fieldNode.getIntValue();
            } else if (propClass.isAssignableFrom(Short.class)) {
              value = Integer.valueOf(fieldNode.getIntValue()).shortValue();
            } else if (propClass.isAssignableFrom(Boolean.class)) {
              value = fieldNode.getBooleanValue();
            } else if (propClass.isAssignableFrom(Vector3.class)) {
              Vector3 vector = new Vector3();
              if (fieldNode.get("x") != null)
                vector.setX(fieldNode.get("x").getDoubleValue());
              if (fieldNode.get("y") != null)
                vector.setY(fieldNode.get("y").getDoubleValue());
              if (fieldNode.get("z") != null)
                vector.setZ(fieldNode.get("z").getDoubleValue());
            } else if (propClass.isAssignableFrom(List.class) || propClass.isAssignableFrom(Set.class)) {
              Field javaField = result.getClass().getDeclaredField(field);
              ParameterizedType pt = (ParameterizedType) javaField.getGenericType();
              Class<?> genericType = (Class<?>) pt.getActualTypeArguments()[0];
              if (classes.contains(genericType)) {
                Collection<Object> col = null;
                if (propClass.isAssignableFrom(List.class)) {
                  col = new ArrayList<Object>();
                } else {
                  col = new HashSet<Object>();
                }
                Iterator<JsonNode> nodes = fieldNode.getElements();
                while (nodes.hasNext()) {
                  JsonNode colNode = (JsonNode) nodes.next();
                  Object element = genericType.newInstance();
                  parseAll(element, colNode, classes);
                  col.add(element);
                }
                value = col;
              }
            } else if (propClass.isEnum()) {
              value = fieldNode.getTextValue();
              Class<? extends Enum<?>> enumClass = ( Class<? extends Enum<?>>) propClass;
              Enum<?>[] enums = enumClass.getEnumConstants();
              for (Enum<?> elem : enums) {
                if (elem.toString().equals(value)) {
                  value = elem;
                  break;
                }
              }

            } else if (classes.contains(propClass)) {
              // Self defined Object
              Object element = propClass.newInstance();
              parseAll(element, fieldNode, classes);
              value = element;
            } else {
              log.warn("Ignored parsing Class: " + propClass.getSimpleName());
            }
            if (value != null) {
              PropertyUtils.setProperty(result, field, value);
            }
          } else {
            log.warn(String.format("Field <%s> not found in Bean <%s>", field, result.getClass().getSimpleName()));
          }
        } catch (Exception e) {
          log.error("", e);
        }
      }
    }
  }

  private String getRealField(String name, Object result) {
    Field target = null;
    try {
      target = result.getClass().getField(name);
    } catch (NoSuchFieldException e) {
    } catch (SecurityException e) {
    }
    if (target == null) {
      Field[] fields = result.getClass().getDeclaredFields();
      for (Field field : fields) {
        if (field.isAnnotationPresent(JsonFieldName.class)) {
          JsonFieldName mapping = field.getAnnotation(JsonFieldName.class);
          String newName = mapping.name();
          if (newName.equals(name)) {
            //Sdasd
            log.info("Return new name " + field.getName());
            return field.getName();
          }
        }
      }
    }
    log.error("No Class Field found for JSON Field name " + name);
    return name;
  }
}
