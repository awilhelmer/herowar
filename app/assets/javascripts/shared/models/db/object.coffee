app = require 'application'

class Object3D extends Backbone.Model 

	url: ->
		"#{app.resourcePath()}object/#{@id}"
	
return Object3D