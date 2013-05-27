events = require 'events'

class ViewerControls extends THREE.TrackballControls
	
	constructor: (@view) ->
		super @view.get('cameraScene'), @view.get('domElement')
		@target.set 0, 50, 0
		events.on 'engine:render', @update, @

return ViewerControls