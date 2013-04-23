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
		if event.which is 1
			switch @active
				when Constants.TOOL_SELECTION
					if !Variables.MOUSE_MOVED
						@selectorObject.update()
				when Constants.TOOL_BRUSH
					EditorEventbus.resetWireframe.dispatch @selectorObject.selectedObject 
		null

	onMouseMove: =>
		@selectorArea.update() if @active is Constants.TOOL_BRUSH
		
	selectTool: (type) =>
		@active = type

return Tools