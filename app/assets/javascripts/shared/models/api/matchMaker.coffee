app = require 'application'
db = require 'database'

class APIMatchMaker extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/#{@get('id')}"			

return APIMatchMaker