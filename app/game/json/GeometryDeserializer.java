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
        mat.setDbgColor(elem.get("DbgColor").getIntValue());
        mat.setDbgIndex(elem.get("DbgIndex").getIntValue());
        mat.setDbgName(elem.get("DbgName").getTextValue());
        mat.setColorAmbient(elem.get("colorAmbient").toString());
        mat.setColorDiffuse(elem.get("colorDiffuse").toString());
        mat.setColorSpecular(elem.get("colorSpecular").toString());
        materials.add(mat);
      }
    }
    return new Geometry(geometryId, geometryNode.get("vertices").toString(), geometryNode.get("faces").toString(), geometryNode.get("morphTargets").toString(),
        geometryNode.get("morphColors").toString(), geometryNode.get("normals").toString(), geometryNode.get("colors").toString(), geometryNode.get("uvs")
            .toString(), geometryNode.get("scale").getDoubleValue(), GeometryType.ENVIRONMENT, metadata, materials);
  }
}
