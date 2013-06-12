app = require 'application'
db = require 'database'

class APIMatch extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/create/#{@get('mapId')}" unless @has 'id'
		return "#{app.resourcePath()}game/match/join/#{@get('id')}" 
		

return APIMatch