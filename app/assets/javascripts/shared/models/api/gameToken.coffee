app = require 'application'
db = require 'database'

class APIGameToken extends Backbone.Model

	url: ->
		"#{app.resourcePath()}game/gametoken/#{@id}"

return APIGameToken