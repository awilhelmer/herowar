BaseView = require 'views/baseView'
templates = require 'templates'

class MenuBarView extends BaseView

	id: 'menubar'
	
	tagName: 'ul'
		
	template: templates.get 'menubar.tmpl'
	
return MenuBarView