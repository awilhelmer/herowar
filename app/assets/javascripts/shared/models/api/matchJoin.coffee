app = require 'application'
db = require 'database'

class APIMatchJoin extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/join/#{@get('mapId')}" unless @has 'id'

return APIMatchJoin