class MatchesRecent extends Backbone.Collection

	model: require 'models/ui/match'

	comparator: (match) ->
		return - match.get 'cdate'

return MatchesRecent