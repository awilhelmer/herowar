package game.json.excludes;

import game.json.MatchResultSimpleSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Sebastian Sachtleben
 */
@JsonSerialize(using = MatchResultSimpleSerializer.class)
public class MatchResultSimpleMixin {

}
