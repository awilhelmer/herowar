BaseModalView = require 'views/baseModalView'
templates = require 'templates'

class ModalFileMapSave extends BaseModalView

	id: 'modalFileMapSave'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapSave.tmpl'

return ModalFileMapSave