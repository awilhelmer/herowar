BaseView = require 'views/baseView'
templates = require 'templates'

class ViewportsView extends BaseView

	id: 'viewports'
	
	entity: 'ui/viewports'
	
	template: templates.get 'viewports.tmpl'
	
	getTemplateData: ->
		json = super()
		for view in json
			view.rotation_deg = []
			view.rotation_deg.push Math.round view.rotation[0] * 180 / Math.PI
			view.rotation_deg.push Math.round view.rotation[1] * 180 / Math.PI
			view.rotation_deg.push Math.round view.rotation[2] * 180 / Math.PI
		json
	
return ViewportsView