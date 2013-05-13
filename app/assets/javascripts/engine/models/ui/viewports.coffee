class Viewports extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/ui/viewport'
		super models, options

return Viewports