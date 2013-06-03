class Waves extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/wave'
		super models, options

return Waves