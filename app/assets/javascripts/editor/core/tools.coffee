EditorEventbus = require 'editorEventbus'
IntersectHelper = require 'helper/intersectHelper'
ObjectHelper = require 'helper/objectHelper'
AddEnvironment = require 'tools/addEnvironment'
BrushMaterial = require 'tools/brushMaterial'
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
		@addEnvironment = new AddEnvironment @editor, @intersectHelper
		@selectorObject = new SelectorObject @editor, @objectHelper, @intersectHelper
		@brushMaterial = new BrushMaterial @editor, @intersectHelper, @selectorObject	
	
	addEventListeners: ->
		EditorEventbus.mouseup.add @onMouseUp
		EditorEventbus.mousemove.add @onMouseMove
	
	onMouseUp: (event) =>
		@[@tool.get('active')].onMouseUp event
	
	onMouseMove: =>
		@[@tool.get('active')].onMouseMove()
	
return Tools