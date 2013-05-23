IntersectHelper = require 'helper/intersectHelper'
log = require 'util/logger'
events = require 'events'
db = require 'database'

class Tools

	defaultTool: null

	constructor: ->
		@initialize()
		
	initialize: ->
		log.debug 'Initialize tools'
		@tool = db.get 'ui/tool'
		@input = db.get 'input'
		log.debug 'Set Tool Selection'
		@tool.set 'active', @defaultTool
		@createHelpers()
		@createTools()
		@addEventListeners()
	
	createHelpers: ->
		@intersectHelper = new IntersectHelper()
	
	createTools: ->
	
	addEventListeners: ->
		events.on 'mouse:up', @onMouseUp, @
		events.on 'mouse:move', @onMouseMove, @
	
	onMouseUp: (event) ->
		@[@tool.get('active')].onMouseUp event if @tool.get('active')
		@switchTool @defaultTool if event.which is 3
	
	onMouseMove: ->
		@[@tool.get('active')].onMouseMove() if @tool.get('active')

	switchTool: (tool) ->
		if @tool.get('active') isnt tool
			@[@tool.get('active')].onLeaveTool() if @tool.get('active')
			@tool.set 'active', tool

return Tools