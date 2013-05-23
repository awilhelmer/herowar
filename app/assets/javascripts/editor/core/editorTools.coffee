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
		@objectHelper = new ObjectHelper()
		@pathingHelper = new PathingHelper()
		super()

	createTools: ->
		@addEnvironment = new AddEnvironment  @intersectHelper
		@addWaypoint = new AddWaypoint @intersectHelper
		@selectorObject = new SelectorObject @objectHelper, @intersectHelper
		@brushMaterial = new BrushMaterial @intersectHelper, @selectorObject	

return EditorTools