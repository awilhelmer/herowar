package game.json;

import java.io.IOException;

import models.entity.game.Material;
import models.entity.game.Texture;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

public class MaterialDeserializer extends BaseDeserializer<Material> {

	@Override
	public Material deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode geometryNode = oc.readTree(jp);
		Material mat = this.parseObject(geometryNode, Texture.class);

		return mat;
	}

}
