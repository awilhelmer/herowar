FormView = require 'views/formView'
templates = require 'templates'
app = require 'application'

class EditFormView extends FormView
	
	entityType: ''
	
	formType: ''
	
	url: '/dummy'
	
	events:
		'click .cancel-button'	: 'cancel'
		'submit form'						: 'submitForm'
	
	initialize: (options) ->
		super options
		@formType = @$el.attr 'data-type'
		@$el.removeAttr 'data-type'
		throw 'The entityType should be set.' unless @entityType
		throw 'The formType should be set.' unless @formType
		if @formType is 'new'
			@url = "/api/#{@entityType}"
		else 
			@url = "/api/#{@entityType}/#{@model.id}"
			@type = 'PUT'
	
	getTemplateData: ->
		json = super()
		json.isNew = true if @formType is 'new'
		json
	
	render: ->
		super()
		@fillForm(@$el.find('form'))
		
	cancel: (event) ->
		event?.preventDefault()
		app.navigate "admin/#{@entityType}/all", true 		

	onSuccess: (data, textStatus, jqXHR) ->
		title = 'New' if @formType is 'new'
		title = 'Edit' unless @formType is 'new'
		status = 'created' if @formType is 'new'
		status = 'updated' unless @formType is 'new'
		$.gritter.add
			title: "#{title} #{@entityType}",
			text: "The #{@entityType} has been successfully #{status}."
		app.navigate "admin/#{@entityType}/all", true 
		
	fillForm: ($Form) ->
		$.each($Form.find('input, select, textarea'), (i, n) =>
			if n.type == 'checkbox'
				$(n).attr('checked', @model.get(n.name))
			else
				$(n).val(@model.get(n.name))
		)

return EditFormView