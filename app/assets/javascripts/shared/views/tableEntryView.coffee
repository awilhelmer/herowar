BaseView = require 'views/baseView'

###
    The TableEntryView shows a row inside of a table.

    @author Sebastian Sachtleben
###
class TableEntryView extends BaseView

	events:
		'click .edit-link'		: 'editEntry'
		'click .delete-link'	: 'deleteEntry'
		
	editEntry: (event) ->
		console.log "Edit #{@model.id}"
		
	deleteEntry: (event) ->
		console.log "Delete entry #{@model.id}"

return TableEntryView