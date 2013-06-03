class Paths extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/path'
		super models, options

return Paths