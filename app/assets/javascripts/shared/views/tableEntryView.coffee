BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'
db = require 'database'

###
    The TableEntryView shows a row inside of a table.

    @author Sebastian Sachtleben
###
class TableEntry extends BaseView

	template: templates.get 'tableEntry.tmpl'

	entityType: ''

	deleteField: 'name'

	events:
		'click .edit-link'		: 'editEntry'
		'click .delete-link'	: 'deleteEntry'
	
	initialize: (options) ->
		@fields = @$el.attr 'data-fields'
		@entity = @$el.attr 'data-entity'
		@$el.removeAttr 'data-fields data-entity'
		super options
		
	getTemplateData: ->
		vals = []
		parts = @fields.split ','
		vals.push @model.get entry for entry in parts
		vals
		
	editEntry: (event) ->
		event?.preventDefault()
		app.navigate "admin/#{@entityType}/#{@model.id}", true
		
	deleteEntry: (event) ->
		event?.preventDefault()
		throw 'entityType should be set to delete entry' unless @entityType
		if confirm("Do you really want to delete the #{@entityType} \"#{@model.get(@deleteField)}\"?")
			$.ajax
				type: 'DELETE'
				url: "/#{@entityType}/#{@model.id}"
				success: (data, textStatus, jqXHR) =>
					$.gritter.add
						title: "Delete #{@entityType}",
						text: "The #{@entityType} \"#{@model.get(@deleteField)}\" has been successfully deleted."
					collection = db.get "api/#{@entityType}s"
					collection.fetch()

return TableEntry