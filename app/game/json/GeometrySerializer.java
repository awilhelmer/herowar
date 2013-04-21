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
    writeNumberField(jgen, "id", geometry.getId());
    writeStringAsDoubleArray(jgen, "vertices", geometry.getVertices());
    writeStringAsDoubleArray(jgen, "faces", geometry.getFaces());
    writeStringAsDoubleArray(jgen, "morphTargets", geometry.getMorphTargets());
    writeStringAsDoubleArray(jgen, "morphColors", geometry.getMorphColors());
    writeStringAsDoubleArray(jgen, "normals", geometry.getNormals());
    writeStringAsDoubleArray(jgen, "colors", geometry.getColors());
    writeStringAsDoubleMultiArray(jgen, "uvs", geometry.getUvs());
    writeNumberField(jgen, "scale", geometry.getScale());
  }
  
  private void writeMetadata(GeoMetaData metadata, JsonGenerator jgen) throws JsonGenerationException, IOException {
    writeNumberField(jgen, "id", metadata.getId());
    writeNumberField(jgen, "formatVersion", metadata.getFormatVersion());
    jgen.writeStringField("sourceFile", metadata.getSourceFile());
    jgen.writeStringField("generatedBy", metadata.getGeneratedBy());
    writeNumberField(jgen, "vertices", metadata.getVertices());
    writeNumberField(jgen, "faces", metadata.getFaces());
    writeNumberField(jgen, "normals", metadata.getNormals());
    writeNumberField(jgen, "colors", metadata.getColors());
    writeNumberField(jgen, "uvs", metadata.getUvs());
    writeNumberField(jgen, "materials", metadata.getMaterials());
  }
}
