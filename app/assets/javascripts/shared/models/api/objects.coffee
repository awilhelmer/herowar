app = require 'application'
db = require 'database'

class APIObjects extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/object'
		super models, options

	url: '/api/object/all'

	parse: (resp) ->
		db.add "db/objects", entry for entry in resp
		return resp
    
return APIObjects