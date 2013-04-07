Constants = require 'constants'

class World extends Backbone.Model

	initialize: (options) ->
		@reset()
		
	reset: ->		
		@set 
			'skybox' : Constants.WORLD_DEFAULT_SKYBOX

	addSelectionWireframe: ->

	removeSelectionWireframe: ->

return World