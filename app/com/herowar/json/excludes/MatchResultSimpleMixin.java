package com.herowar.json.excludes;


import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.herowar.json.MatchResultSimpleSerializer;

/**
 * @author Sebastian Sachtleben
 */
@JsonSerialize(using = MatchResultSimpleSerializer.class)
public class MatchResultSimpleMixin {

}
