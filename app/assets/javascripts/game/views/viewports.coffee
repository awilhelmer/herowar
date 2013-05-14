BaseView = require 'views/baseView'
templates = require 'templates'

class ViewportsView extends BaseView

	id: 'viewports'
	
	entity: 'ui/viewports'
	
	template: templates.get 'viewports.tmpl'
	
	getTemplateData: ->
		json = super()
		for view in json
			view.rotation[0] = Math.round view.rotation[0] * (180 / Math.PI)
			view.rotation[1] = Math.round view.rotation[1] * (180 / Math.PI)
			view.rotation[2] = Math.round view.rotation[2] * (180 / Math.PI)
		json
	
return ViewportsView