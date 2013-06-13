app = require 'application'
db = require 'database'

class APIMatchCreate extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/create/#{@get('mapId')}" unless @has 'id'

return APIMatchCreate