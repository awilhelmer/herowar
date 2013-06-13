app = require 'application'
db = require 'database'

class APIMatchJoin extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/join"

	parse: (resp) ->
		matchMaker = db.get 'api/matchMaker'
		matchMaker.clear()
		matchMaker.set resp
		return resp

return APIMatchJoin