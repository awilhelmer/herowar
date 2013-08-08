package com.herowar.game.json;

import java.io.IOException;


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

import com.herowar.models.entity.game.Material;

public class MaterialSerializer extends BaseSerializer<Material> {

	@Override
	public void serialize(Material value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		writeAll(jgen, value);
	}

}
