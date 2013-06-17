app = require 'application'
db = require 'database'

class APIMatchMaker extends Backbone.Model

	url: ->
		return "/api/game/match/#{@get('id')}"			

return APIMatchMaker