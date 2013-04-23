EditorEventbus = require 'editorEventbus'
MaterialHelper = require 'helper/materialHelper'
IntersectHelper = require 'helper/intersectHelper'
ObjectHelper = require 'helper/objectHelper'
SelectorArea = require 'helper/selectorArea'
SelectorObject = require 'helper/selectorObject'
Constants = require 'constants'
Variables = require 'variables'

class Tools

	list : [ Constants.TOOL_SELECTION, Constants.TOOL_BRUSH	]

	active : Constants.TOOL_SELECTION	
	
	constructor: (@editor) ->
		@initialize()
		
	initialize: ->
		console.log 'Initialize tools'
		@createHelpers()
		@createSelectors()
		@addEventListeners()
	
	createHelpers: ->
		@objectHelper = new ObjectHelper @editor
		@intersectHelper = new IntersectHelper @editor
		@materialHelper = new MaterialHelper @editor
	
	createSelectors: ->
		@selectorObject = new SelectorObject @editor, @materialHelper, @objectHelper, @intersectHelper
		@selectorArea = new SelectorArea @editor, @materialHelper, @intersectHelper, @selectorObject	
	
	addEventListeners: ->
		EditorEventbus.mouseup.add @onMouseUp
		EditorEventbus.mousemove.add @onMouseMove
		EditorEventbus.selectTool.add @selectTool
	
	onMouseUp: (event) =>
		switch @active
			when Constants.TOOL_SELECTION
				@selectorObject.onMouseUp event
			when Constants.TOOL_BRUSH
				if event.which is 1
					EditorEventbus.resetWireframe.dispatch @selectorObject.selectedObject 

	onMouseMove: =>
		switch @active
			when Constants.TOOL_SELECTION
				@selectorObject.onMouseMove()
			when Constants.TOOL_BRUSH
				@selectorArea.update()
		
	selectTool: (type) =>
		@active = type

return Tools