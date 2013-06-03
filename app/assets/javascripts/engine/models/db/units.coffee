class Units extends Backbone.Collection

	url: '/api/game/unit/root'

	initialize: (models, options) ->
		@model = require 'models/db/unit'
		super models, options

return Units