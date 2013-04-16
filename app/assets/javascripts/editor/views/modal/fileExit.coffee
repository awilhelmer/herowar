BaseModalView = require 'views/baseModalView'
templates = require 'templates'

class ModalFileExit extends BaseModalView

	id: 'modalFileExit'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileExit.tmpl'

	events:
		'click .btn-primary' : 'exit'
	
	exit: (event) ->
		event?.preventDefault()
		@hide()
		window.location = '/'
	
return ModalFileExit