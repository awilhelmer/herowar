BaseView = require 'views/baseView'
templates = require 'templates'

class VictoryView extends BaseView

	id: 'victory'
		
	template: templates.get 'victory.tmpl'

	events:
		'click .next' : 'next'

	next: (event) ->
		event.preventDefault()

return VictoryView