app = require 'application'

class Map extends Backbone.Model 

	url: ->
		"/game/map/#{@id}"
	
return Map