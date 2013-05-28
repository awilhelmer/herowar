BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

class Home extends BaseView

	id: 'home'
	
	template: templates.get 'home.tmpl'
	
return Home