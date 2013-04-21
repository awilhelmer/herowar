EditorEventbus = require 'editorEventbus'
SelectorArea = require 'helper/selectorArea'
SelectorGeometry = require 'helper/selectorGeometry'
SelectorObject = require 'helper/selectorObject'
Constants = require 'constants'
Variables = require 'variables'
MaterialHelper = require 'helper/materialHelper'

class Tools

	list : [ Constants.TOOL_SELECTION, Constants.TOOL_BRUSH	]

	active : Constants.TOOL_SELECTION	
	
	constructor: (@editor) ->
		@initialize()
		
	initialize: ->
		console.log 'Initialize tools'
		@materialHelper = new MaterialHelper @editor
		@selectorArea = new SelectorArea @editor, @materialHelper,@selectorObject
		@selectorGeometry = new SelectorGeometry @editor
		@selectorObject = new SelectorObject @editor, @materialHelper
		@addEventListeners()
	
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