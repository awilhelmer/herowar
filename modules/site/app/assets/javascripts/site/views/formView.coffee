BaseView = require 'views/baseView'
app = require 'application'

###
    The FormView contains several helper to handle a form inside of a view.

    @author Sebastian Sachtleben
###
class FormView extends BaseView

	id: 'signup'
	
	type: 'POST'
	
	dataType: 'json'
	
	url: ''
	
	events:
		"submit form": 'submitForm'
	
	initialize: (options) ->
		@requestInProgress = false
		@$Form = @$el.find 'form'
		throw "FormView should contain a form" unless @form and @form.length > 0
		throw "FormView should contain a url" unless @url
		super options
	
	submitForm: (event) ->
		event?.preventDefault()
		if !@requestInProgress
			@requestInProgress = true
			@onFormSubmit()
			$.ajax
				dataType: @dataType
				type: @type
				url: @url
				data: @getFormData()
				success: (data, textStatus, jqXHR) =>
					@onSuccess data, textStatus, jqXHR
				error: (jqXHR, textStatus, errorThrown) =>
					@onError jqXHR, textStatus, errorThrown
				complete: (jqXHR, textStatus) =>
					@onComplete jqXHR, textStatus
				
	getFormData: ->
	   $.map $('form input'), (n, i) ->
	       Key: n.name, Value: $(n).val()
	
	onFormSubmit: ->
	       
	onSuccess: (data, textStatus, jqXHR) ->
	
	onError: (jqXHR, textStatus, errorThrown) ->
	
	onComplete: (jqXHR, textStatus) ->
	
return Signup