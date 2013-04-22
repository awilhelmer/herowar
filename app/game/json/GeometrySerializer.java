package game.json;

import java.io.IOException;
import java.util.List;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;
import models.entity.game.Material;

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
    jgen.writeObjectFieldStart("metadata");
    writeMetadata(geometry.getMetadata(), jgen);
    jgen.writeEndObject();
    writeGeometry(geometry, jgen);
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
      writeStringField(jgen, "DbgName", mat.getDbgName());
      writeNumberField(jgen, "DbgColor", mat.getDbgColor());
      writeNumberField(jgen, "DbgIndex", mat.getDbgIndex());
      writeStringAsDoubleArray(jgen, "colorAmbient", mat.getColorAmbient());
      writeStringAsDoubleArray(jgen, "colorDiffuse", mat.getColorDiffuse());
      writeStringAsDoubleArray(jgen, "colorSpecular", mat.getColorSpecular());
      writeNumberField(jgen, "id", mat.getId());
      writeStringField(jgen, "mapDiffuse", mat.getMapDiffuse());
      writeStringField(jgen, "name", mat.getName());
      writeStringField(jgen, "blending", mat.getBlending());
      writeStringField(jgen, "shading", mat.getShading());
      writeBooleanField(jgen, "depthTest", mat.getDepthTest());
      writeBooleanField(jgen, "depthWrite", mat.getDepthWrite());
      writeBooleanField(jgen, "transparent", mat.getTransparent());
      writeBooleanField(jgen, "vertexColors", mat.getVertexColors());
      writeNumberField(jgen, "transparency", mat.getTransparency());
      writeNumberField(jgen, "specularCoef", mat.getSpecularCoef());
      writeStringAsStringArray(jgen, "mapDiffuseWrap", mat.getMapDiffuseWrap());
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
