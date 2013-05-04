EditorEventbus = require 'editorEventbus'
IntersectHelper = require 'helper/intersectHelper'
ObjectHelper = require 'helper/objectHelper'
PlaceObject = require 'tools/placeObject'
SelectorArea = require 'tools/selectorArea'
SelectorObject = require 'tools/selectorObject'
Constants = require 'constants'
Variables = require 'variables'
log = require 'util/logger'

class Tools

	list : [ Constants.TOOL_SELECTION, Constants.TOOL_BRUSH	]

	active : Constants.TOOL_SELECTION	
	
	constructor: (@editor) ->
		@initialize()
		
	initialize: ->
		log.info 'Initialize tools'
		@createHelpers()
		@createSelectors()
		@addEventListeners()
	
	createHelpers: ->
		@objectHelper = new ObjectHelper @editor
		@intersectHelper = new IntersectHelper @editor
	
	createSelectors: ->
		@placeObject = new PlaceObject @editor
		@selectorObject = new SelectorObject @editor, @objectHelper, @intersectHelper
		@selectorArea = new SelectorArea @editor, @intersectHelper, @selectorObject	
	
	addEventListeners: ->
		EditorEventbus.mouseup.add @onMouseUp
		EditorEventbus.mousemove.add @onMouseMove
		EditorEventbus.selectTool.add @selectTool
	
	onMouseUp: (event) =>
		switch @active
			when Constants.TOOL_SELECTION
				@selectorObject.onMouseUp event
			when Constants.TOOL_BRUSH
				@selectorArea.onMouseUp event

	onMouseMove: =>
		switch @active
			when Constants.TOOL_SELECTION
				@selectorObject.onMouseMove()
			when Constants.TOOL_BRUSH
				@selectorArea.onMouseMove()
		
	selectTool: (type) =>
		@active = type

return Tools