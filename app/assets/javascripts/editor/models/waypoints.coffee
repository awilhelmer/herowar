class Waypoints extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/waypoint'
		super models, options

return Waypoints