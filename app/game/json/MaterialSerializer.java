package game.json;

import java.io.IOException;

import models.entity.game.Material;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

public class MaterialSerializer extends BaseSerializer<Material> {

  @Override
  public void serialize(Material value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    writeAll(jgen, value);
  }

}
