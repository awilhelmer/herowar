BaseView = require 'views/baseView'
templates = require 'templates'

class DebugView extends BaseView

	id: 'debug'
		
	template: templates.get 'debug.tmpl'
	
return DebugView