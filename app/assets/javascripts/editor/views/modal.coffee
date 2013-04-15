BaseView = require 'views/baseView'
templates = require 'templates'

class Modal extends BaseView
	
	template: templates.get 'modal.tmpl'
	
return Modal