package game.json;

import java.io.IOException;

import models.entity.game.Player;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

public class PlayerWithUsernameSerializer extends BaseSerializer<Player> {

  @Override
  public void serialize(Player player, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    jgen.writeStartObject();
    writeNumberField(jgen, "id", player.getId());
    writeStringField(jgen, "username", player.getUser().getUsername());
    jgen.writeEndObject();
  }

}