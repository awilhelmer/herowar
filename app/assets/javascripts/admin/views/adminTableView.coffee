AdminAuthView = require 'views/adminAuthView'

###
    The AdminTableView provides basic functionalities to edit entries in a table view.

    @author Sebastian Sachtleben
###
class AdminTableView extends AdminAuthView

	events:
		'click .edit-link'		: 'editEntry'
		'click .delete-link'	: 'deleteEntry'
	
	initialize: (options) ->
		super options
		@model.fetch()
		
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model
		
	editEntry: (event) ->
		console.log 'Edit entry'
		
	deleteEntry: (event) ->
		console.log 'Delete entry'

return AdminTableView