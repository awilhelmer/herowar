BaseView = require 'views/baseView'
templates = require 'templates'
Variables = require 'variables'

class CameraView extends BaseView

	className: 'camera'

	entity: 'ui/viewports'

	events:
		'click' : 'switchCamera'
		
	switchCamera: (event) ->
		view = @model.at 0
		camera = view.get 'camera'
		camera.type = if camera.type is Variables.CAMERA_TYPE_ORTHOGRAPHIC then Variables.CAMERA_TYPE_PERSPECTIVE else Variables.CAMERA_TYPE_ORTHOGRAPHIC
		view.trigger 'change:camera'
		view.trigger 'change'
		view.createCameraScene()
		view.updateCamera()
		return

return CameraView