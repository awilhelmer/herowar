app = require 'application'
db = require 'database'

class APIMatch extends Backbone.Model

	url: ->
		return "/game/match/create/#{@get('mapId')}" unless @has 'id'
		return "/game/match/join/#{@get('id')}" 
		

return APIMatch