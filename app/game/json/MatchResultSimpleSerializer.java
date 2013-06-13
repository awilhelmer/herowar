package game.json;

import java.io.IOException;

import models.entity.game.Map;
import models.entity.game.MatchResult;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

public class MatchResultSimpleSerializer extends BaseSerializer<MatchResult> {

  @Override
  public void serialize(MatchResult value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    jgen.writeStartObject();
    writeNumberField(jgen, "score", value.getScore());
    if (value.getMatch() != null) {
      writeNumberField(jgen, "cdate", value.getMatch().getCdate().getTime());
      writeNumberField(jgen, "lives", value.getMatch().getLives());
      writeStringField(jgen, "state", value.getMatch().getState().name());
      writeBooleanField(jgen, "victory", value.getMatch().getVictory());
      if (value.getMatch().getMap() != null) {
        Map map = value.getMatch().getMap();
        jgen.writeObjectFieldStart("map");
        writeNumberField(jgen, "id", map.getId());
        writeStringField(jgen, "name", map.getName());
        writeNumberField(jgen, "lives", map.getLives());
        jgen.writeEndObject();
      }
    }
    jgen.writeEndObject();
  }

}
