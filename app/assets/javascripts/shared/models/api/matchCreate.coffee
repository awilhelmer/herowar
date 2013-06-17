app = require 'application'
db = require 'database'

class APIMatchCreate extends Backbone.Model

	url: ->
		return "/api/game/match/create/#{@get('mapId')}" unless @has 'id'

	parse: (resp) ->
		matchMaker = db.get 'api/matchMaker'
		matchMaker.clear()
		resp.host = true if resp.id
		matchMaker.set resp
		return resp

return APIMatchCreate