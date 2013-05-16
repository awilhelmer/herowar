class Towers extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/tower'
		super models, options

return Towers