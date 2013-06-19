IntersectHelper = require 'helper/intersectHelper'
log = require 'util/logger'
events = require 'events'
db = require 'database'

class Tools

	defaultTools: []

	constructor: ->
		@initialize()
		
	initialize: ->
		log.debug 'Initialize tools'
		@tool = db.get 'ui/tool'
		@input = db.get 'input'
		log.debug 'Set Tool Selection'
		@tool.set 'active', {}
		@createHelpers()
		@createTools()
		@addListeners()
		@reset()
		return
	
	addListeners: ->
		events.on 'tools:add', @addTool, @
		events.on 'tools:switch', @switchTool, @
		events.on 'tools:remove', @removeTool, @
		events.on 'mouse:up', @_onMouseUp, @
		events.on 'mouse:move', @_onMouseMove, @
		return
	
	createHelpers: ->
		@intersectHelper = new IntersectHelper()
		return
	
	createTools: ->
		return

	addTool: (key, tool) ->
		activeTools = @tool.get 'active'
		return if _.has activeTools, key
		activeTools[key] = if _.isUndefined tool then @[key] else tool
		return
	
	switchTool: (key, tool) ->
		@_leaveTool activeKey, activeTool for activeKey, activeTool of @tool.get 'active'
		@tool.set 'active', {}
		@addTool key, tool
		return
	
	removeTool: (key) ->
		activeTools = @tool.get 'active'
		return unless _.has activeTools, key
		@_leaveTool key, activeTools[key]
		delete activeTools[key]
		return
	
	reset: ->
		activeTools = @tool.get 'active'
		@_leaveTool key, tool for key, tool of activeTools when @defaultTools.indexOf(key) is -1
		@tool.set 'active', _.pick activeTools, @defaultTools
		@tool.get('active')[tool] = @[tool] for tool in @defaultTools when not _.has @tool.get('active'), tool
		return

	_leaveTool: (key, tool) ->
		tool.onLeaveTool()
		return
	
	_onMouseUp: (event) ->
		tool.onMouseUp event for key, tool of @tool.get 'active'
		@reset() if event.which is 3
		return
	
	_onMouseMove: ->
		tool.onMouseMove() for key, tool of @tool.get 'active'
		return

return Tools