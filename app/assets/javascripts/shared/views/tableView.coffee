BaseView = require 'views/baseView'
templates = require 'templates'

###
    The TableView provides basic functionalities to edit entries in a table view.

    @author Sebastian Sachtleben
###
class TableView extends BaseView
	
	template: templates.get 'table.tmpl'
	
	allowCreate: true
	
	entryView: 'views/tableEntryView'
	
	events:
		'click .create-link' : 'createEntry'
	
	initialize: (options) ->
		super options
		@model.fetch()
		
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	getTemplateData: ->
		json = super()
		tableHeaders = []
		tableFields = []
		if _.isObject @fields
			for own key, value of @fields 
				tableHeaders.push key
				tableFields.push value
			tableHeaders.push 'Actions'
			json.tableHeaders = tableHeaders
			json.tableFields = tableFields.join ','
		json.entity = @tableEntity
		json.entryView = @entryView
		json.allowCreate = @allowCreate
		json.colspan = tableFields.length + 1
		json
		
	createEntry: (event) ->
		event?.preventDefault()

return TableView