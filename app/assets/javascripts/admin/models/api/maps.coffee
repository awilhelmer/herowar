app = require 'application'
db = require 'database'

###
	APIMaps provides a collection of user fetched by '/api/map/all'.

	@author Sebastian Sachtleben
###
class APIMaps extends Backbone.Collection

	initialize: (models, options) ->
		@model = require "models/db/map"
		super models, options

	url: ->
		"#{app.resourcePath()}map/all"

	parse: (resp) ->
		db.add "db/maps", entry for entry in resp
		return resp
    
return APIMaps