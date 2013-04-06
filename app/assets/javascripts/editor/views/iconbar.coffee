BaseView = require 'views/baseView'
templates = require 'templates'

class IconbarView extends BaseView

	id: 'iconbar'
	
	template: templates.get 'iconbar.tmpl'
	
return IconbarView