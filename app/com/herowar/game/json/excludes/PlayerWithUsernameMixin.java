package com.herowar.game.json.excludes;


import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.herowar.game.json.PlayerWithUsernameSerializer;

@JsonSerialize(using = PlayerWithUsernameSerializer.class)
public class PlayerWithUsernameMixin {

}
