BaseView = require 'views/baseView'
templates = require 'templates'

class Menubar extends BaseView

	id: 'menubar'
	
	className: 'navbar navbar-fixed-top'
	
	template: templates.get 'menubar.tmpl'
	
	events:
		'click [data-modal="true"]'		: 'showModal'
		'click #fileMapNewGenerated'	: 'fileNewMapGenerated'

	showModal: (event) ->
		unless event then return
		event?.preventDefault()
		$currentTarget = $ event.currentTarget
		id = $currentTarget.attr 'id'
		$("#modal#{id.charAt(0).toUpperCase()}#{id.slice(1)}").modal 'show'

	fileNewMapGenerated: (event) ->
		event?.preventDefault()
		alert 'Not implemented yet...'

return Menubar