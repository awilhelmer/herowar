app = require 'application'

class Map extends Backbone.Model 

	url: ->
		"#{app.resourcePath()}game/map/#{@id}"
	
return Map