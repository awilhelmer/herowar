package game.json;

import java.io.IOException;

import models.entity.game.Geometry;
import models.entity.game.GeometryType;

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
    JsonNode node = oc.readTree(jsonParser);
    Long id = null;
    if (node.get("id") != null) {
      id = node.get("id").getLongValue();
    }
    return new Geometry(id, node.get("vertices").getTextValue(), node.get("faces").getTextValue(), node.get("morphTargets")
        .getTextValue(), node.get("morphColors").getTextValue(), node.get("normals").getTextValue(), node.get("colors").getTextValue(), node.get("uvs")
        .getTextValue(), node.get("scale").getDoubleValue(), GeometryType.ENVIRONMENT, null);
  }

}
