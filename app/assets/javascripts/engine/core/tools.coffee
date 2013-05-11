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
		@[@tool.get('active')].onMouseUp event
	
	onMouseMove: ->
		@[@tool.get('active')].onMouseMove()
	
return Tools