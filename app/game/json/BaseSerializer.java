package game.json;

import java.io.IOException;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;

/**
 * The BaseSerializer provides several methods which helps to serialize a pojo properly.
 * 
 * @author Sebastian Sachtleben
 * 
 * @param <T>
 *          The class to serialize
 */
public abstract class BaseSerializer<T> extends JsonSerializer<T> {

  protected void writeStringAsDoubleArray(JsonGenerator jgen, ArrayConverter arrayConverter, String name, String value) throws JsonGenerationException, IOException {
    if (value != null && !"".equals(value)) {
      value = value.substring(1);
      value = value.substring(0, value.length() - 1);
      String[] parts = value.split(",");
      if (parts == null || parts.length == 0) {
        return;
      }
      jgen.writeArrayFieldStart(name);
      double[] values = (double[]) arrayConverter.convert(double[].class, parts);
      for (int i = 0; i < values.length; i++) {
        jgen.writeNumber(values[i]);        
      }      
      jgen.writeEndArray();
    }    
  }

  protected void writeNumberField(JsonGenerator jgen, String name, Long value) throws JsonGenerationException, IOException {
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
