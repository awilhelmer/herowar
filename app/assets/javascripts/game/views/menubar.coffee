BaseView = require 'views/baseView'
templates = require 'templates'

class MenuBarView extends BaseView

	id: 'menubar'
		
	template: templates.get 'menubar.tmpl'
	
return MenuBarView