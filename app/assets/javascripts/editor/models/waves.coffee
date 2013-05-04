db = require 'database'

class Waves extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/wave'
		super models, options

return Waves