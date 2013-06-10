class Towers extends Backbone.Collection

	url: '/api/game/tower/all'
	
	comparator: (tower) ->
		return tower.get 'price'

	initialize: (models, options) ->
		@model = require 'models/db/tower'
		super models, options

return Towers