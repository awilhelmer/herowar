package game.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;
import models.entity.game.Material;

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
    jgen.writeArrayFieldStart("materials");
    writeMaterials(geometry.getMaterials(), jgen);
    jgen.writeEndArray();
    jgen.writeEndObject();
  }

  private void writeMaterials(List<Material> materials, JsonGenerator jgen) throws JsonGenerationException, IOException {
    if (materials == null || materials.size() == 0) {
      return;
    }
    for (Material mat : materials) {
      jgen.writeStartObject();
      if (mat.getDbgName() != null) {
        jgen.writeStringField("name", mat.getDbgName());
      }
      writeNumberField(jgen, "DbgIndex", mat.getDbgIndex());
      writeStringAsDoubleArray(jgen, "colorAmbient", mat.getColorAmbient());
      writeStringAsDoubleArray(jgen, "colorDiffuse", mat.getColorDiffuse());
      writeStringAsDoubleArray(jgen, "colorSpecular", mat.getColorSpecular());
      writeNumberField(jgen, "id", mat.getId());
      if (mat.getName() != null) {
        jgen.writeStringField("name", mat.getName());
      }
      jgen.writeEndObject();
    }
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
