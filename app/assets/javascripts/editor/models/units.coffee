class Units extends Backbone.Collection

	url: '/api/game/unit/root'

	initialize: (models, options) ->
		@model = require 'models/unit'
		super models, options

return Units