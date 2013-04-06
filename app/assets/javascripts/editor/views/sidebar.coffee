BaseView = require 'views/baseView'
templates = require 'templates'

class Sidebar extends BaseView

	id: 'sidebar'
	
	className: 'sidebar-right'
	
	template: templates.get 'sidebar.tmpl'
	
return Sidebar