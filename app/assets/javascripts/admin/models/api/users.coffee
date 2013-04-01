app = require 'application'
db = require 'database'

###
	Users provides a collection of user fetched by '/api/users/all'.

	@author Sebastian Sachtleben
###
class Users extends Backbone.Collection

	initialize: (models, options) ->
		@model = require "models/db/user"
		super models, options

	url: ->
		"#{app.resourcePath()}user/all"

	parse: (resp) ->
		db.add "db/users", entry for entry in resp
		return resp
    
return Users