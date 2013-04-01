AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

class MapsEdit extends AdminAuthView

	entity: 'db/maps'
	
	template: templates.get 'maps/edit.tmpl'
	
	events:
		'click .save-button'		: 'saveEntry'
		'click .cancel-button'	: 'cancel'
	
	initialize: (options) ->
		super options
		console.log @model
		
	saveEntry: (event) ->
		event?.preventDefault()
		console.log 'Save maps here'
		
	cancel: (event) ->
		event?.preventDefault()
		app.navigate 'admin/user/all', true 		

return MapsEdit