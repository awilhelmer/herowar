BaseView = require 'views/baseView'
templates = require 'templates'

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
		console.log 'switchCamera'

	getTemplateData: ->
		json = super()
		view.camera.typeName = @modes[view.camera.type] for view in json
		console.log 'CameraView getTemplate()', json
		json

return CameraView