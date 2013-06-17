app = require 'application'

class Object3D extends Backbone.Model 

	url: ->
		"/object/#{@id}"
	
return Object3D