class Materials extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/material'
		super models, options

return Materials