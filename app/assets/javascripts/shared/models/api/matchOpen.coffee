app = require 'application'
db = require 'database'

class APIMatchOpen extends Backbone.Model

	url: ->
		return "/game/match/find"			

return APIMatchOpen