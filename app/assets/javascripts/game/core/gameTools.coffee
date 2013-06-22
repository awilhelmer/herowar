Tools = require 'core/tools'
events = require 'events'

class GameTools extends Tools
	
	defaultTools: [ 'selectTower', 'selectUnit' ]
	
	tools: [ 'addTower', 'selectTower', 'selectUnit' ]
	
	addListeners: ->
		events.on 'select:tower', @onSelectTower, @
		super()
	
	createTools: ->
		@[tool] = new (require "tools/#{tool}") @intersectHelper for tool in @tools
		return

	onSelectTower: ->
		@addTool 'addTower', @addTower
		return

return GameTools