app = require 'application'
db = require 'database'

class APINews extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/news'
		super models, options

	url: '/api/news/all'

	parse: (resp) ->
		db.add "db/newss", entry for entry in resp
		return resp
    
return APINews