EditorEventbus = require 'editorEventbus'
BaseModalView = require 'views/baseModalView'
templates = require 'templates'

class ModalFileMapNewEmpty extends BaseModalView

	id: 'modalFileMapNewEmpty'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapNewEmpty.tmpl'

	events:
		'click .btn-primary' : 'newMapEmpty'
	
	newMapEmpty: (event) =>
		event?.preventDefault()
		@hide()
		window.location = '/editor'

return ModalFileMapNewEmpty