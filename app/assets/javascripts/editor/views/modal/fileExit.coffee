BaseView = require 'views/baseView'
templates = require 'templates'

class ModalFileExit extends BaseView

	id: 'modelFileExit'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileExit.tmpl'
	
return ModalFileExit