EditorEventbus = require 'editorEventbus'
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
	
	defaultTools: [ Constants.TOOL_SELECTION ]

	bindEvents: ->
		EditorEventbus.initIdChanged.add @setStartId

	createHelpers: ->
		@objectHelper = new ObjectHelper()
		@pathingHelper = new PathingHelper()
		super()

	createTools: ->
		@addEnvironment = new AddEnvironment  @intersectHelper
		@addWaypoint = new AddWaypoint @intersectHelper
		@selectorObject = new SelectorObject @objectHelper, @intersectHelper
		@brushMaterial = new BrushMaterial @intersectHelper, @selectorObject	

	setStartId: (module, startId) =>
		@addWaypoint.nextId = startId if module is 'waypoint'

return EditorTools