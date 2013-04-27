package game.json;

import java.io.IOException;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;
import models.entity.game.GeometryType;
import models.entity.game.Material;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

import dao.game.GeoMatId;

/**
 * Custom Deserializer for Geometry entity. We have to parse the arrays to
 * strings.
 * 
 * @author Sebastian Sachtleben
 */
public class GeometryDeserializer extends BaseDeserializer<Geometry> {

  @Override
  public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode geometryNode = oc.readTree(jsonParser);
    Geometry geo = this.parseObject(geometryNode, GeoMetaData.class, Material.class, GeoMatId.class);
    geo.setType(GeometryType.ENVIRONMENT);
    for (Material mat : geo.getMaterials()) {
      mat.setName(mat.getDbgName());
    }
    return geo;
  }
}
