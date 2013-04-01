FormView = require 'views/formView'
templates = require 'templates'
app = require 'application'

class MapNewForm extends FormView
	
	template: templates.get 'maps/newForm.tmpl'
	
	url: "#{app.resourcePath()}map"
	
	events:
		'click .cancel-button'	: 'cancel'
		'submit form'						: 'submitForm'
		
	cancel: (event) ->
		event?.preventDefault()
		app.navigate 'admin/map/all', true 		

	onSuccess: (data, textStatus, jqXHR) ->
		$.gritter.add
			title: 'New Map',
			text: 'The map has been successfully created.'
		app.navigate 'admin/map/all', true 	
	
	onError: (jqXHR, textStatus, errorThrown) ->
		console.log 'Error'

return MapNewForm