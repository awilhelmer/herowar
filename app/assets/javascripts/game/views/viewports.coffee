BaseView = require 'views/baseView'
templates = require 'templates'

class ViewportsView extends BaseView

	id: 'viewports'
	
	entity: 'ui/viewports'
	
	template: templates.get 'viewports.tmpl'
	
return ViewportsView