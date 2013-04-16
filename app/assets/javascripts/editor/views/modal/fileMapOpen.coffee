BaseView = require 'views/baseView'
templates = require 'templates'

class ModalFileMapOpen extends BaseView

	id: 'modalFileMapOpen'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapOpen.tmpl'

return ModalFileMapOpen