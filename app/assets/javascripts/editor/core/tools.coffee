EditorEventbus = require 'editorEventbus'
IntersectHelper = require 'helper/intersectHelper'
ObjectHelper = require 'helper/objectHelper'
PlaceObject = require 'tools/placeObject'
SelectorArea = require 'tools/selectorArea'
SelectorObject = require 'tools/selectorObject'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class Tools

	constructor: (@editor) ->
		@initialize()
		
	initialize: ->
		log.debug 'Initialize tools'
		@tool = db.get 'ui/tool'
		log.debug 'Set Tool Selection'
		@tool.set 'active', Constants.TOOL_SELECTION
		@createHelpers()
		@createSelectors()
		@addEventListeners()
	
	createHelpers: ->
		@objectHelper = new ObjectHelper @editor
		@intersectHelper = new IntersectHelper @editor
	
	createSelectors: ->
		@placeObject = new PlaceObject @editor, @intersectHelper
		@selectorObject = new SelectorObject @editor, @objectHelper, @intersectHelper
		@selectorArea = new SelectorArea @editor, @intersectHelper, @selectorObject	
	
	addEventListeners: ->
		EditorEventbus.mouseup.add @onMouseUp
		EditorEventbus.mousemove.add @onMouseMove
	
	onMouseUp: (event) =>
		switch @tool.get 'active'
			when Constants.TOOL_BUILD
				@placeObject.onMouseUp event
			when Constants.TOOL_BRUSH
				@selectorArea.onMouseUp event
			when Constants.TOOL_SELECTION
				@selectorObject.onMouseUp event
	
	onMouseMove: =>
		switch @tool.get 'active'
			when Constants.TOOL_BUILD
				@placeObject.onMouseMove()
			when Constants.TOOL_BRUSH
				@selectorArea.onMouseMove()
	
return Tools