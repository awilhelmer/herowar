ObjectHelper = require 'helper/objectHelper'
PathingHelper = require 'helper/pathingHelper'
AddEnvironment = require 'tools/addEnvironment'
AddWaypoint = require 'tools/addWaypoint'
BrushMaterial = require 'tools/brushMaterial'
SelectorObject = require 'tools/selectorObject'
Constants = require 'constants'
Tools = require 'core/tools'
log = require 'util/logger'

class EditorTools extends Tools
	
	defaultTool: Constants.TOOL_SELECTION

	createHelpers: ->
		@objectHelper = new ObjectHelper @app
		@pathingHelper = new PathingHelper @app
		super()

	createTools: ->
		@addEnvironment = new AddEnvironment @app, @intersectHelper
		@addWaypoint = new AddWaypoint @app, @intersectHelper
		@selectorObject = new SelectorObject @app, @objectHelper, @intersectHelper
		@brushMaterial = new BrushMaterial @app, @intersectHelper, @selectorObject	

	onMouseUp: (event) ->
		super event
		if event.which is 3 and @tool.get('active') isnt @defaultTool
			@[@tool.get('active')].onLeaveTool()
			log.debug 'Set Tool Selection'
			@tool.set 'active', @defaultTool

return EditorTools