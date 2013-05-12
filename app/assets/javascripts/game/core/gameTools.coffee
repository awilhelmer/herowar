AddTower = require 'tools/addTower'
Tools = require 'core/tools'
events = require 'events'

class GameTools extends Tools
	
	addEventListeners: ->
		events.on 'select:tower', @onSelectTower, @
		super()
	
	createTools: ->
		@addTower = new AddTower @app, @intersectHelper

	onSelectTower: ->
		@switchTool 'addTower'

return GameTools