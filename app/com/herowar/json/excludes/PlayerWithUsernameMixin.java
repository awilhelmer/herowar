package com.herowar.json.excludes;


import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.herowar.json.PlayerWithUsernameSerializer;

@JsonSerialize(using = PlayerWithUsernameSerializer.class)
public class PlayerWithUsernameMixin {

}
