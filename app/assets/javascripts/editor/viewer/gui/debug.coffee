BaseGUI = require 'viewer/gui/base'

class DebugGUI extends BaseGUI
	
	debug:
		boundingBox: false
	
	constructor: (@target) ->
		super 'Debug'

	create: ->
		@root = @parent.addFolder @name
		@children['boundingBox'] = @root.add(@debug, 'boundingBox').listen().onChange =>
			if @debug.boundingBox then @target.showBoundingBox() else @target.hideBoundingBox() if @target
		return @root
	
return DebugGUI