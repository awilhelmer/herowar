app = require 'application'
db = require 'database'

###
	APIUsers provides a collection of user fetched by '/api/user/all'.

	@author Sebastian Sachtleben
###
class APIUsers extends Backbone.Collection

	initialize: (models, options) ->
		@model = require "models/db/user"
		super models, options

	url: ->
		"#{app.resourcePath()}user/all"

	parse: (resp) ->
		db.add "db/users", entry for entry in resp
		return resp
    
return APIUsers