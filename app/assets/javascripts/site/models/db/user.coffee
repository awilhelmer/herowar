app = require 'application'

class Account extends Backbone.Model 

	url: ->
		"#{app.resourcePath()}user/#{@id}"
	
return Account