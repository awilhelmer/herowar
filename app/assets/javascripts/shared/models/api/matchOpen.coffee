app = require 'application'
db = require 'database'

class APIMatchOpen extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/find"			

return APIMatchOpen