app = require 'application'

class News extends Backbone.Model 

	url: ->
		"/news/#{@id}"
	
return News