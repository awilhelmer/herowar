package com.herowar.json;

import java.io.IOException;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

import com.herowar.dao.GeoMatId;
import com.herowar.models.entity.game.GeoMetaData;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.Material;


/**
 * Custom Deserializer for Geometry entity. We have to parse the arrays to strings.
 * 
 * @author Sebastian Sachtleben
 */
public class GeometryDeserializer extends BaseDeserializer<Geometry> {

	@Override
	public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
			JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode geometryNode = oc.readTree(jsonParser);
		Geometry geo = this.parseObject(geometryNode, GeoMetaData.class, Material.class, GeoMatId.class);
		// geo.setType(GeometryType.ENVIRONMENT);
		if (geo.getMaterials() != null) {
			for (Material mat : geo.getMaterials()) {
				// Here we have got uploaded Materials, so any name is missing
				if (mat.getName() == null)
					mat.setName(mat.getDbgName());

			}
		}
		return geo;
	}
}
