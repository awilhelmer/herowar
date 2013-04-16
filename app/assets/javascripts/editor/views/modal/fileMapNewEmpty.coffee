EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class ModalFileMapNewEmpty extends BaseView

	id: 'modalFileMapNewEmpty'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapNewEmpty.tmpl'

	events:
		'click .btn-primary' : 'newMapEmpty'
	
	newMapEmpty: (event) =>
		event?.preventDefault()
		$('#modelFileMapNewEmpty').modal 'hide'
		EditorEventbus.newMapEmpty.dispatch()

return ModalFileMapNewEmpty