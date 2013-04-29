BaseView = require 'views/baseView'
templates = require 'templates'

class SidebarLeft extends BaseView

	id: 'sidebar-left'
	
	template: templates.get 'sidebarLeft.tmpl'
	
return SidebarLeft