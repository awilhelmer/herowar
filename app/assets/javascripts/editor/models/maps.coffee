db = require 'database'

class Maps extends Backbone.Collection

	url: '/api/map/all'

	initialize: (models, options) ->
		@model = require 'models/map'
		super models, options

return Maps