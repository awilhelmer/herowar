package game.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;
import models.entity.game.GeometryType;
import models.entity.game.Material;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * Custom Deserializer for Geometry entity. We have to parse the arrays to
 * strings.
 * 
 * @author Sebastian Sachtleben
 */
public class GeometryDeserializer extends JsonDeserializer<Geometry> {

  @Override
  public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode geometryNode = oc.readTree(jsonParser);

    // Parse metadata
    GeoMetaData metadata = null;
    JsonNode metadataNode = geometryNode.get("metadata");
    if (metadataNode != null) {
      Long metadataId = null;
      if (metadataNode.get("id") != null) {
        metadataId = metadataNode.get("id").getLongValue();
      }
      metadata = new GeoMetaData(metadataId, (float) metadataNode.get("formatVersion").getDoubleValue(), metadataNode.get("sourceFile").getTextValue(),
          metadataNode.get("generatedBy").getTextValue(), metadataNode.get("vertices").getLongValue(), metadataNode.get("faces").getLongValue(), metadataNode
              .get("normals").getLongValue(), metadataNode.get("colors").getLongValue(), metadataNode.get("uvs").getLongValue(), metadataNode.get("materials")
              .getLongValue());
    }

    // Parse geometry
    Long geometryId = null;
    if (geometryNode.get("id") != null) {
      geometryId = geometryNode.get("id").getLongValue();
    }
    List<Material> materials = new ArrayList<Material>();
    if (geometryNode.get("materials") != null) {
      Iterator<JsonNode> nodes = geometryNode.get("materials").getElements();
      while (nodes.hasNext()) {
        JsonNode elem = (JsonNode) nodes.next();
        Material mat = new Material();
        mat.setDbgName(elem.get("DbgName").getTextValue());
        mat.setName(mat.getDbgName());
        mat.setDbgIndex(elem.get("DbgIndex").getIntValue());
        if (elem.get("DbgColor") != null)
          mat.setDbgColor(elem.get("DbgColor").getIntValue());
        if (elem.get("colorAmbient") != null)
          mat.setColorAmbient(elem.get("colorAmbient").toString());
        if (elem.get("colorDiffuse") != null)
          mat.setColorDiffuse(elem.get("colorDiffuse").toString());
        if (elem.get("colorSpecular") != null)
          mat.setColorSpecular(elem.get("colorSpecular").toString());
        if (elem.get("mapDiffuse") != null)
          mat.setMapDiffuse(elem.get("mapDiffuse").getTextValue());
        if (elem.get("blending") != null)
          mat.setBlending(elem.get("blending").getTextValue());
        if (elem.get("depthTest") != null)
          mat.setDepthTest(elem.get("depthTest").getBooleanValue());
        if (elem.get("depthWrite") != null)
          mat.setDepthWrite(elem.get("depthWrite").getBooleanValue());
        if (elem.get("mapDiffuseWrap") != null)
          mat.setMapDiffuseWrap(elem.get("mapDiffuseWrap").toString());
        if (elem.get("transparency") != null)
          mat.setTransparency(elem.get("transparency").getDecimalValue().floatValue());
        if (elem.get("transparent") != null)
          mat.setTransparent(elem.get("transparent").getBooleanValue());
        if (elem.get("vertexColors") != null)
          mat.setVertexColors(elem.get("vertexColors").getBooleanValue());
        if (elem.get("shading") != null)
          mat.setShading(elem.get("shading").getTextValue());
        if (elem.get("specularCoef") != null)
          mat.setSpecularCoef(elem.get("specularCoef").getIntValue());

        materials.add(mat);
      }
    }
    return new Geometry(geometryId, geometryNode.get("vertices").toString(), geometryNode.get("faces").toString(), geometryNode.get("morphTargets").toString(),
        geometryNode.get("morphColors").toString(), geometryNode.get("normals").toString(), geometryNode.get("colors").toString(), geometryNode.get("uvs")
            .toString(), geometryNode.get("scale").getDoubleValue(), GeometryType.ENVIRONMENT, metadata, materials);
  }
}
