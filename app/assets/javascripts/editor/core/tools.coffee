EditorEventbus = require 'editorEventbus'
IntersectHelper = require 'helper/intersectHelper'
ObjectHelper = require 'helper/objectHelper'
PathingHelper = require 'helper/pathingHelper'
AddEnvironment = require 'tools/addEnvironment'
AddWaypoint = require 'tools/addWaypoint'
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
		@pathingHelper = new PathingHelper @editor
	
	createSelectors: ->
		@addEnvironment = new AddEnvironment @editor, @intersectHelper
		@addWaypoint = new AddWaypoint @editor, @intersectHelper
		@selectorObject = new SelectorObject @editor, @objectHelper, @intersectHelper
		@brushMaterial = new BrushMaterial @editor, @intersectHelper, @selectorObject	
	
	addEventListeners: ->
		EditorEventbus.mouseup.add @onMouseUp
		EditorEventbus.mousemove.add @onMouseMove
	
	onMouseUp: (event) =>
		@[@tool.get('active')].onMouseUp event
		if event.which is 3 and @tool.get('active') isnt Constants.TOOL_SELECTION
			@[@tool.get('active')].onLeaveTool()
			log.debug 'Set Tool Selection'
			@tool.set 'active', Constants.TOOL_SELECTION
	
	onMouseMove: =>
		@[@tool.get('active')].onMouseMove()
	
return Tools