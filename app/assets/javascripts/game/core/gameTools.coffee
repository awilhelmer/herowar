AddTower = require 'tools/addTower'
Tools = require 'core/tools'
events = require 'events'

class GameTools extends Tools
	
	tools: [ 'tools/addTower', 'tools/selectTower' ]
	
	addEventListeners: ->
		events.on 'select:tower', @onSelectTower, @
		super()
	
	createTools: ->
		@[tool.substring(tool.lastIndexOf('/') + 1)] = new (require tool) @intersectHelper for tool in @tools
		return

	onSelectTower: ->
		@switchTool 'addTower'
		return

return GameTools