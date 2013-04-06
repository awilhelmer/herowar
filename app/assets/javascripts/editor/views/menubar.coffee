BaseView = require 'views/baseView'
templates = require 'templates'

class Menubar extends BaseView

	id: 'menubar'
	
	className: 'navbar navbar-fixed-top'
	
	template: templates.get 'menubar.tmpl'
	
return Menubar