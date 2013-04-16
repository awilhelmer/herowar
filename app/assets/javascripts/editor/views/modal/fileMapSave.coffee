BaseView = require 'views/baseView'
templates = require 'templates'

class ModalFileMapSave extends BaseView

	id: 'modalFileMapSave'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapSave.tmpl'

return ModalFileMapSave