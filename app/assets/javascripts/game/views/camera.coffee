BaseView = require 'views/baseView'
templates = require 'templates'
Variables = require 'variables'

class CameraView extends BaseView

	id: 'camera'
	
	entity: 'ui/viewports'
	
	template: templates.get 'camera.tmpl'

	modes: [ 'Orthograpgic', 'Perspective' ]

	events:
		'click .switch' : 'switchCamera'
	
	initialize: (options) ->
		super options
		console.log 'CameraView model=', @model
	
	switchCamera: (event) ->
		view = @model.at 0
		camera = view.get 'camera'
		camera.type = if camera.type is Variables.CAMERA_TYPE_ORTHOGRAPHIC then Variables.CAMERA_TYPE_PERSPECTIVE else Variables.CAMERA_TYPE_ORTHOGRAPHIC
		view.trigger 'change:camera'
		view.trigger 'change'
		view.createCameraScene()
		view.updateSize()
		return

	getTemplateData: ->
		json = super()
		view.camera.typeName = @modes[view.camera.type] for view in json
		console.log 'CameraView getTemplate()', json
		json

return CameraView