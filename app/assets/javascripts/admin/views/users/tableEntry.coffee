TableEntryView = require 'views/tableEntryView'

###
    The UsersTableEntry shows a row in the user table.

    @author Sebastian Sachtleben
###
class UsersTableEntry extends TableEntryView

	editEntry: (event) ->
		console.log "Edit #{@model.id}"
		
	deleteEntry: (event) ->
		console.log "Delete entry #{@model.id}"

return UsersTableEntry