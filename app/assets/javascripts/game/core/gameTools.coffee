Tools = require 'core/tools'
events = require 'events'

class GameTools extends Tools
	
	defaultTools: [ 'selectTower' ]
	
	tools: [ 'selectTower', 'addTower' ]
	
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