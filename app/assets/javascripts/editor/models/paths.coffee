db = require 'database'

class Paths extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/path'
		super models, options

return Paths