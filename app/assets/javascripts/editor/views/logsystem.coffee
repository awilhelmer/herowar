BaseView = require 'views/baseView'
templates = require 'templates'

class LogSystem extends BaseView

	id: 'logsystem'
	
	entity: 'ui/logs'
	
	template: templates.get 'logsystem.tmpl'
	
	events:
		'click .minimize' : 'minimize'
		'click .maximize' : 'maximize'
	
	initalize: (options) ->
		@isMinimized = false
	
	afterRender: ->
		container = @$ '.content'
		container.animate scrollTop: container[0].scrollHeight
	
	minimize: ->
		console.log 'Minimize'
		$viewport = $ '#viewport'
		$viewport.animate bottom: "-=72px", 500
		@$el.animate height: "-=72px", 500
		@isMinimized = true
		@render()
	
	maximize: ->
		console.log 'Maximize'
		$viewport = $ '#viewport'
		$viewport.animate bottom: "+=72px", 500
		@$el.animate height: "+=72px", 500
		@isMinimized = false
		@render()
	
	getTemplateData: ->
		json = super()
		json.isMinimized = @isMinimized
		json
	
return LogSystem