class Maps extends Backbone.Collection

	url: '/api/game/map/all'

	initialize: (models, options) ->
		@model = require 'models/map'
		super models, options

return Maps