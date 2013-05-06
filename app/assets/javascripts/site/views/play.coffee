AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'

class Play extends AuthView

	id: 'play'
	
	entity: 'db/maps'
	
	template: templates.get 'play.tmpl'
	
	getTemplateData: ->
		json = super()
		console.log json
		json
	
return Play