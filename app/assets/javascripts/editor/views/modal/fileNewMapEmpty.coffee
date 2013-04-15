BaseView = require 'views/baseView'
templates = require 'templates'

class ModalFileNewMapEmpty extends BaseView

	id: 'modelFileNewMapEmpty'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileNewMapEmpty.tmpl'
	
return ModalFileNewMapEmpty