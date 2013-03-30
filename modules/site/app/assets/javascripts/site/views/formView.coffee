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
		throw "FormView should contain a url" unless @url
		super options
	
	submitForm: (event) ->
		event?.preventDefault()
		unless @$Form
			@$Form = @$el.find 'form' 
			throw "FormView should contain a form" unless @$Form and @$Form.length > 0
		if !@requestInProgress
			@requestInProgress = true
			@$Form.find('.btn').addClass 'disabled'
			@onFormSubmit()
			$.ajax
				dataType: @dataType
				type: @type
				url: @url
				data: @getFormData @$Form
				success: (data, textStatus, jqXHR) =>
					@onSuccess data, textStatus, jqXHR
				error: (jqXHR, textStatus, errorThrown) =>
					@onError jqXHR, textStatus, errorThrown
				complete: (jqXHR, textStatus) =>
					@onComplete jqXHR, textStatus
				
	getFormData: ($Form) ->
		data = {}
		$.each($Form.find('input'), (i, n) ->
			data[n.name] = $(n).val()
		)
		data
	
	onFormSubmit: ->
	       
	onSuccess: (data, textStatus, jqXHR) ->
	
	onError: (jqXHR, textStatus, errorThrown) ->
	
	onComplete: (jqXHR, textStatus) ->
		@requestInProgress = false
		@$Form.find('.btn').removeClass 'disabled'
	
return FormView