app = require 'application'
db = require 'database'

class APIMatchToken extends Backbone.Model

	url: ->
		return "#{app.resourcePath()}game/match/join/#{@get('id')}" 
		

return APIMatchToken