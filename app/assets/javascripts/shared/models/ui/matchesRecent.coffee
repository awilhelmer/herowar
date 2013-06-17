app = require 'application'

class MatchesRecent extends Backbone.Collection

	model: require 'models/ui/match'

	url: ->
		"/game/match/history"

	###
	comparator: (match) ->
		return - match.get 'cdate'
	###

return MatchesRecent