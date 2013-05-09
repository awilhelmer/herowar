BaseView = require 'views/baseView'
templates = require 'templates'

class ProgressView extends BaseView

	id: 'progress'
	
	entity: 'ui/stats'
	
	template: templates.get 'progress.tmpl'
	
return ProgressView