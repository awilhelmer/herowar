BaseView = require 'views/baseView'
app = require 'application'

###
    The FormView contains several helper to handle a form inside of a view.

    @author Sebastian Sachtleben
###
class FormView extends BaseView
	
	type: 'POST'
	
	dataType: 'json'
	
	url: ''
	
	events:
		'submit form': 'submitForm'
	
	initialize: (options) ->
		@requestInProgress = false
		throw "FormView should contain a url" unless @url
		super options
	
	submitForm: (event) ->
		event?.preventDefault()
		unless @$Form
			@$Form = @$el.find 'form' 
			throw "FormView should contain a form" unless @$Form and @$Form.length > 0
		if !@requestInProgress && @validateForm(@$Form)
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
	
	validateForm: ($Form) ->
		true
				
	getFormData: ($Form) ->
		data = {}
		$.each($Form.find('input:not(:disabled), select, textarea'), (i, n) ->
			if n.type == 'checkbox'
				data[n.name] = $(n).is(':checked')
			else
				data[n.name] = $(n).val()
		)
		data
	
	onFormSubmit: ->
	       
	onSuccess: (data, textStatus, jqXHR) ->
	
	onError: (jqXHR, textStatus, errorThrown) ->
	
	onComplete: (jqXHR, textStatus) ->
		@requestInProgress = false
		@$Form.find('.btn').removeClass 'disabled'
		
	setInputState: ($Input, type, text) ->
		$OptionGroup = $Input.closest '.control-group'
		$Controls = $OptionGroup.find '.controls'
		$Help = $Controls.find '.help-inline'
		$OptionGroup.removeClass('warning').removeClass('error').removeClass('info').removeClass('success')
		$OptionGroup.addClass(type) if type
		$Help.remove()
		$Controls.append('<span class="help-inline">' + text + '</span>') if text
	
return FormView