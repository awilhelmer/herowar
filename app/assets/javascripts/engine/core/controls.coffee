EditorControls = require 'controls/editorControls'
RTSControls = require 'controls/rtsControls'
ViewerControls = require 'controls/viewerControls'
Variables = require 'variables'
Eventbus = require 'eventbus'
db = require 'database'

class Controls
	
	constructor: ->
		@controls = []
		@initialize()

	initialize: ->
		viewports = db.get 'ui/viewports'
		for view in viewports.models
			switch view.get 'type'
				when Variables.VIEWPORT_TYPE_RTS
					console.log 'Create RTSControls for', view
					@controls.push new RTSControls view
				when Variables.VIEWPORT_TYPE_EDITOR
					# TODO: refactor this ...
					console.log 'Create EditorControls for', view
					@controls.push new EditorControls view
					Eventbus.controlsChanged.add @onControlsChanged
				when Variables.VIEWPORT_TYPE_VIEWER
					console.log 'Create ViewerControls for', view
					@controls.push new ViewerControls view
		return
	
	onControlsChanged: (event) =>
		control.update() for control in @controls
		return

return Controls