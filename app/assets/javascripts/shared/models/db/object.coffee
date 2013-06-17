app = require 'application'

class Object3D extends Backbone.Model 

	url: ->
		"/api/object/#{@id}"
	
return Object3D