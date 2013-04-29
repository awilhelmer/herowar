BaseView = require 'views/baseView'
templates = require 'templates'

class LogSystem extends BaseView

	id: 'logsystem'
	
	entity: 'ui/logs'
	
	template: templates.get 'logsystem.tmpl'
	
	afterRender: ->
		container = @$ '.content'
		container.animate scrollTop: container[0].scrollHeight
	
return LogSystem