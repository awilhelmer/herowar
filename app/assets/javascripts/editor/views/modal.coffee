BaseView = require 'views/baseView'
templates = require 'templates'

class Modal extends BaseView
	
	id: 'modals'
	
	template: templates.get 'modal.tmpl'
	
return Modal