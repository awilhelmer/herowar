app = require 'application'
db = require 'database'

class APIUsers extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/user'
		super models, options

	url: '/api/user/all'

	parse: (resp) ->
		db.add "db/users", entry for entry in resp
		return resp
    
return APIUsers