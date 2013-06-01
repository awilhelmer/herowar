Eventbus = require 'eventbus'

class EditorControls extends THREE.TrackballControls
	
	constructor: (@view) ->
		super @view.get('cameraScene'), @view.get('domElement')
		@rotateSpeed = 1.0
		@zoomSpeed = 1.2
		@panSpeed = 0.8
		@noZoom = false
		@noPan = false
		@noRotate = false
		@staticMoving = true
		@dynamicDampingFactor = 0.3 
		@enabled = true
		@controlLoopActive = false
		@addEventListener( 'change', =>
			Eventbus.cameraChanged.dispatch view
			null
		)
		@update()

return EditorControls