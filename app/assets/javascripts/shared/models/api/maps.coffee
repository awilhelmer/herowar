app = require 'application'
db = require 'database'

class APIMaps extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/map'
		super models, options

	url: ->
		"#{app.resourcePath()}game/map/all"

	parse: (resp) ->
		db.add "db/maps", entry for entry in resp
		return resp
    
return APIMaps