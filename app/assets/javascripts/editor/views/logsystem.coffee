BaseView = require 'views/baseView'
templates = require 'templates'

class LogSystem extends BaseView

	id: 'logsystem'
	
	template: templates.get 'logsystem.tmpl'
	
return LogSystem