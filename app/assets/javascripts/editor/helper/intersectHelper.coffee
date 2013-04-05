Variables = require 'variables'

class IntersectHelper

	constructor: (@editor) ->
		@ray = new THREE.Raycaster()
		@projector = new THREE.Projector()
		
	mouseIntersects: (objects) ->
		vector = new THREE.Vector3(
			((Variables.MOUSE_X - Variables.SCREEN_LEFT) / Variables.SCREEN_WIDTH) * 2 - 1
			-((Variables.MOUSE_Y - Variables.SCREEN_TOP) / Variables.SCREEN_HEIGHT) * 2 + 1
			0.5)
		camera = @editor.engine().viewhandler.views[0].camera	# TODO: find out from which viewport this click comes
		@projector.unprojectVector vector, camera
		@ray.set camera.position, vector.sub(camera.position).normalize()
		@ray.intersectObjects objects, true

return IntersectHelper