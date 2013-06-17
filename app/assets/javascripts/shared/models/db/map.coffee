app = require 'application'

class Map extends Backbone.Model 

	url: ->
		"/api/game/map/#{@id}"
	
return Map