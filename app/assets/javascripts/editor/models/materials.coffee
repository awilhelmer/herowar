db = require 'database'

class Material extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/material'
		super models, options

return Material