app = require 'application'
db = require 'database'

class APIMatchCreate extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/create/#{@get('mapId')}" unless @has 'id'

	parse: (resp) ->
		matchMaker = db.get 'api/matchMaker'
		matchMaker.clear()
		matchMaker.set resp
		return resp

return APIMatchCreate