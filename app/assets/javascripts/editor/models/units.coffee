class Units extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/unit'
		super models, options

return Units