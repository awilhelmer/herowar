EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class ModalFileNewMapEmpty extends BaseView

	id: 'modelFileNewMapEmpty'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileNewMapEmpty.tmpl'

	events:
		'click .btn-primary' : 'newMapEmpty'
	
	newMapEmpty: (event) =>
		event?.preventDefault()
		$('#modelFileNewMapEmpty').modal 'hide'
		EditorEventbus.newMapEmpty.dispatch()

return ModalFileNewMapEmpty