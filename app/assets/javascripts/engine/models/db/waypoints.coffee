class Waypoints extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/db/waypoint'
		super models, options

return Waypoints