BaseView = require 'views/baseView'
templates = require 'templates'

class LogSystem extends BaseView

	id: 'logsystem'
	
	entity: 'ui/logs'
	
	template: templates.get 'logsystem.tmpl'
	
return LogSystem