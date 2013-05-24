BaseView = require 'views/baseView'
templates = require 'templates'

class DefeatView extends BaseView

	id: 'defeat'
		
	template: templates.get 'defeat.tmpl'

	events:
		'click .next' : 'next'

	next: (event) ->
		event.preventDefault()

return DefeatView