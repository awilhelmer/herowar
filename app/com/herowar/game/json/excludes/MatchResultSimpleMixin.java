package com.herowar.game.json.excludes;


import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.herowar.game.json.MatchResultSimpleSerializer;

/**
 * @author Sebastian Sachtleben
 */
@JsonSerialize(using = MatchResultSimpleSerializer.class)
public class MatchResultSimpleMixin {

}
