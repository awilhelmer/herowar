app = require 'application'

class News extends Backbone.Model 

	url: ->
		"/api/news/#{@id}"
	
return News