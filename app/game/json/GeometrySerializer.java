package game.json;

import java.io.IOException;

import models.entity.game.GeoMetaData;
import models.entity.game.Geometry;
import models.entity.game.Material;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

import dao.game.GeoMatId;

/**
 * @author Sebastian Sachtleben
 */
public class GeometrySerializer extends BaseSerializer<Geometry> {

  @Override
  public void serialize(Geometry geometry, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

    writeAll(jgen, geometry, GeoMetaData.class, Material.class, GeoMatId.class);
 
  }
}
