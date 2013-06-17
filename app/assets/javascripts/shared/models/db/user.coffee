app = require 'application'

class Account extends Backbone.Model 

	url: ->
		"/user/#{@id}"
	
return Account