IntersectHelper = require 'helper/intersectHelper'
log = require 'util/logger'
events = require 'events'
db = require 'database'

class Tools

	defaultTool: null

	constructor: (@app) ->
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
		@intersectHelper = new IntersectHelper @app
	
	createTools: ->
	
	addEventListeners: ->
		events.on 'mouse:up', @onMouseUp, @
		events.on 'mouse:move', @onMouseMove, @
	
	onMouseUp: (event) ->
		@[@tool.get('active')].onMouseUp event if @tool.get('active')
		if event.which is 3 and @tool.get('active') isnt @defaultTool
			@[@tool.get('active')].onLeaveTool() if @tool.get('active')
			log.debug 'Set default tool'
			@tool.set 'active', @defaultTool
	
	onMouseMove: ->
		@[@tool.get('active')].onMouseMove() if @tool.get('active')
	
return Tools