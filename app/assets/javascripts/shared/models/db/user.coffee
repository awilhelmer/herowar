app = require 'application'

class Account extends Backbone.Model 

	url: ->
		"/api/user/#{@id}"
	
return Account