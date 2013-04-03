BaseView = require 'views/baseView'
templates = require 'templates'

class HomeAbout extends BaseView
	
	template: templates.get 'home/about.tmpl'
	
return HomeAbout