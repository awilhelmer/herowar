package game.json;

import java.io.IOException;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @author Sebastian Sachtleben
 */
public class GeometrySerializer extends BaseSerializer<Geometry> {

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
    writeNumberField(jgen, "id", geometry.getId());
    writeStringAsDoubleArray(jgen, arrayConverter, "vertices", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "faces", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "morphTargets", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "morphColors", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "normals", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "colors", geometry.getVertices());
    writeStringAsDoubleArray(jgen, arrayConverter, "uvs", geometry.getVertices());
    writeNumberField(jgen, "scale", geometry.getScale());
  }
  
  private void writeMetadata(GeoMetaData metadata, JsonGenerator jgen) throws JsonGenerationException, IOException {
    writeNumberField(jgen, "id", metadata.getId());
    writeNumberField(jgen, "formatVersion", metadata.getFormatVersion());
    jgen.writeStringField("sourceFile", metadata.getSourceFile());
    writeNumberField(jgen, "vertices", metadata.getVertices());
    writeNumberField(jgen, "faces", metadata.getFaces());
    writeNumberField(jgen, "normals", metadata.getNormals());
    writeNumberField(jgen, "colors", metadata.getColors());
    writeNumberField(jgen, "uvs", metadata.getUvs());
    writeNumberField(jgen, "materials", metadata.getMaterials());
  }
}
