package com.herowar.json;

import java.io.IOException;


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

import com.herowar.dao.GeoMatId;
import com.herowar.models.entity.game.GeoMetaData;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.Material;


/**
 * @author Sebastian Sachtleben
 */
public class GeometrySerializer extends BaseSerializer<Geometry> {

	@Override
	public void serialize(Geometry geometry, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

		writeAll(jgen, geometry, GeoMetaData.class, Material.class, GeoMatId.class);

	}
}
