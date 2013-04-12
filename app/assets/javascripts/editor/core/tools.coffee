EditorEventbus = require 'editorEventbus'
SelectorArea = require 'helper/selectorArea'
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
		@selectorObject = new SelectorObject @editor
		@selectorArea = new SelectorArea @editor, @materialHelper,@selectorObject
		@addEventListeners()
	
	addEventListeners: ->
		EditorEventbus.mouseup.add @onMouseUp
		EditorEventbus.mousemove.add @onMouseMove
		EditorEventbus.selectTool.add @selectTool
	
	onMouseUp: (event) =>
		@selectorObject.update() if event.which is 1 and !Variables.MOUSE_MOVED and @active is Constants.TOOL_SELECTION	

	onMouseMove: =>
		@selectorArea.update() if @active is Constants.TOOL_BRUSH
		
	selectTool: (type) =>
		@active = type

return Tools