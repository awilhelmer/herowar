EditorEventbus = require 'editorEventbus'
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
		@isMoving = false
		@isMinimized = false
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render
	
	afterRender: ->
		container = @$ '.content'
		container.animate scrollTop: container[0].scrollHeight, 0
	
	minimize: ->
		lastRerenderTween = 100
		@isMoving = true
		$viewport = $ '#viewport'
		$viewport.animate bottom: "-=72px", 500
		@$el.animate height: "-=72px", 
			duration: 500
			step: (now, tween) =>
				if tween.now < (lastRerenderTween - 5)
					lastRerenderTween = tween.now
					EditorEventbus.dispatch 'render', true
			complete: =>
				@isMoving = false
				@isMinimized = true
				EditorEventbus.dispatch 'render', true
				@render()
		@render()
	
	maximize: ->
		lastRerenderTween = 28
		@isMoving = true
		$viewport = $ '#viewport'
		$viewport.animate bottom: "+=72px", 500
		@$el.animate height: "+=72px", 
			duration: 500
			step: (now, tween) =>
				if tween.now > (lastRerenderTween + 5)
					lastRerenderTween = tween.now
					EditorEventbus.dispatch 'render', true
			complete: =>
				@isMoving = false
				@isMinimized = false
				EditorEventbus.dispatch 'render', true
				@render()
		@render()
	
	getTemplateData: ->
		json = super()
		json.isMoving = @isMoving
		json.isMinimized = @isMinimized
		json
	
return LogSystem