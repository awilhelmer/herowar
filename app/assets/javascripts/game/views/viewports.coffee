BaseView = require 'views/baseView'
templates = require 'templates'

class ViewportsView extends BaseView

	id: 'viewports'
	
	entity: 'ui/viewports'
	
	template: templates.get 'viewports.tmpl'
	
	modes: [ 'Orthograpgic', 'Perspective' ]
	
	getTemplateData: ->
		json = super()
		for view in json
			view.camera.typeName = @modes[view.camera.type]
			view.camera.rotation_deg = []
			view.camera.rotation_deg.push Math.round (if view.camera.rotation then view.camera.rotation[0] else 0) * 180 / Math.PI
			view.camera.rotation_deg.push Math.round (if view.camera.rotation then view.camera.rotation[1] else 0) * 180 / Math.PI
			view.camera.rotation_deg.push Math.round (if view.camera.rotation then view.camera.rotation[2] else 0) * 180 / Math.PI
		json
	
return ViewportsView