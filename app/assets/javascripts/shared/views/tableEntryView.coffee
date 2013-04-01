BaseView = require 'views/baseView'
templates = require 'templates'

###
    The TableEntryView shows a row inside of a table.

    @author Sebastian Sachtleben
###
class TableEntry extends BaseView

	template: templates.get 'tableEntry.tmpl'

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
		
	deleteEntry: (event) ->
		event?.preventDefault()

return TableEntry