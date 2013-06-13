package game.json.excludes;

import game.json.PlayerWithUsernameSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = PlayerWithUsernameSerializer.class)
public class PlayerWithUsernameMixin {

}
