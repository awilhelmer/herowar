app = require 'application'

class MatchesRecent extends Backbone.Collection

	model: require 'models/ui/match'

	url: ->
		"#{app.resourcePath()}game/match/history"

	###
	comparator: (match) ->
		return - match.get 'cdate'
	###

return MatchesRecent