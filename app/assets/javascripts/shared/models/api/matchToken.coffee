app = require 'application'
db = require 'database'

class APIMatchToken extends Backbone.Model

	url: ->
		return "/api/game/match/join/#{@get('id')}" 
		

return APIMatchToken