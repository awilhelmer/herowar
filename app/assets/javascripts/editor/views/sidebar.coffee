BaseView = require 'views/baseView'
templates = require 'templates'

class Sidebar extends BaseView

	id: 'sidebar'
	
	template: templates.get 'sidebar.tmpl'
	
return Sidebar