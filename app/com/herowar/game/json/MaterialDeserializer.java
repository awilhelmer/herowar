package com.herowar.game.json;

import java.io.IOException;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

import com.herowar.models.entity.game.Material;
import com.herowar.models.entity.game.Texture;

public class MaterialDeserializer extends BaseDeserializer<Material> {

	@Override
	public Material deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode geometryNode = oc.readTree(jp);
		Material mat = this.parseObject(geometryNode, Texture.class);

		return mat;
	}

}
