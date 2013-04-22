package game.json;

import java.io.IOException;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;

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

  private DoubleConverter doubleConverter = new DoubleConverter();
  private StringConverter stringConverter = new StringConverter();
  private ArrayConverter arrayDoubleConverter = new ArrayConverter(double[].class, doubleConverter);
  private ArrayConverter arrayStringConverter = new ArrayConverter(String[].class, stringConverter);
  
  
  protected void writeStringAsDoubleMultiArray(JsonGenerator jgen, String name, String value) throws JsonGenerationException, IOException {
    if (value != null && !"".equals(value)) {
      value = value.substring(2);
      value = value.substring(0, value.length() - 2);
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
