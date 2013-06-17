app = require 'application'
db = require 'database'

class APIMatch extends Backbone.Model

	url: ->
		return "/api/game/match/create/#{@get('mapId')}" unless @has 'id'
		return "/api/game/match/join/#{@get('id')}" 
		

return APIMatch