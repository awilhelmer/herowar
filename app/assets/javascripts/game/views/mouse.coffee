BaseView = require 'views/baseView'
templates = require 'templates'

class MouseView extends BaseView

	id: 'mouse'
	
	entity: 'input'
	
	template: templates.get 'mouse.tmpl'
	
	bindEvents: ->
		@listenTo @model, 'change:mouse_position_x change:mouse_position_y', @render
	
return MouseView