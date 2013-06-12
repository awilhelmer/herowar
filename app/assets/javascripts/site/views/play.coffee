AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'
db = require 'database'

class Play extends AuthView

	id: 'play'
	
	template: templates.get 'play.tmpl'
	
return Play