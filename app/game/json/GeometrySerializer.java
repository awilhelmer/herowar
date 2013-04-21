package game.json;

import java.io.IOException;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @author Sebastian Sachtleben
 */
public class GeometrySerializer extends JsonSerializer<Geometry> {

  @Override
  public void serialize(Geometry geometry, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    jgen.writeStartObject();
    writeGeometry(geometry, jgen);
    jgen.writeObjectFieldStart("metadata");
    writeMetadata(geometry.getMetadata(), jgen);
    jgen.writeEndObject();
    jgen.writeEndObject();
  }

  private void writeGeometry(Geometry geometry, JsonGenerator jgen) throws JsonGenerationException, IOException {
    DoubleConverter doubleConverter = new DoubleConverter();
    ArrayConverter arrayConverter = new ArrayConverter(double[].class, doubleConverter);
    jgen.writeNumberField("id", geometry.getId());
    writeStringAsDoubleArray(jgen, arrayConverter, "vertices", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "faces", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "morphTargets", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "morphColors", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "normals", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "colors", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "uvs", geometry.getVertices());
    jgen.writeNumberField("scale", geometry.getScale());
  }
  
  private void writeMetadata(GeoMetaData metadata, JsonGenerator jgen) throws JsonGenerationException, IOException {
    jgen.writeNumberField("id", metadata.getId());
    jgen.writeNumberField("formatVersion", metadata.getFormatVersion());
    jgen.writeStringField("sourceFile", metadata.getSourceFile());
    jgen.writeNumberField("vertices", metadata.getVertices());
    jgen.writeNumberField("faces", metadata.getFaces());
    jgen.writeNumberField("normals", metadata.getNormals());
    jgen.writeNumberField("colors", metadata.getColors());
    jgen.writeNumberField("uvs", metadata.getUvs());
    jgen.writeNumberField("materials", metadata.getMaterials());
  }
  
  private void writeStringAsDoubleArray(JsonGenerator jgen, ArrayConverter arrayConverter, String name, String value) throws JsonGenerationException, IOException {
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

}
