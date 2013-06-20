BaseView = require 'views/baseView'
templates = require 'templates'

class InterruptedView extends BaseView

	id: 'interrupted'
		
	template: templates.get 'interrupted.tmpl'

	events:
		'click a' : 'next'

	next: (event) ->
		event.preventDefault()
		Backbone.history.loadUrl 'game'

return InterruptedView