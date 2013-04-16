BaseView = require 'views/baseView'
templates = require 'templates'

class Menubar extends BaseView

	id: 'menubar'
	
	className: 'navbar navbar-fixed-top'
	
	template: templates.get 'menubar.tmpl'
	
	events:
		'click #fileNewMapEmpty'			: 'fileNewMapEmpty'
		'click #fileNewMapGenerated'	: 'fileNewMapGenerated'
		'click #fileOpen'							: 'fileOpen'	
		'click #fileSave'							: 'fileSave'	
		'click #fileExit'							: 'fileExit'	

	fileNewMapEmpty: (event) ->
		event?.preventDefault()
		$('#modelFileNewMapEmpty').modal 'show'

	fileNewMapGenerated: (event) ->
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileOpen: (event) ->
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileSave: (event) ->
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileExit: (event) ->
		event?.preventDefault()
		$('#modelFileExit').modal 'show'

return Menubar