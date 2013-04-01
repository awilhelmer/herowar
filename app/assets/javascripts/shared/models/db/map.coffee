app = require 'application'

class Map extends Backbone.Model 

	url: ->
		"#{app.resourcePath()}map/#{@id}"
	
return Map