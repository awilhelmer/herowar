app = require 'application'

class News extends Backbone.Model 

	url: ->
		"#{app.resourcePath()}news/#{@id}"
	
return News