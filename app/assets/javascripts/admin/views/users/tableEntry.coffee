TableEntryView = require 'views/tableEntryView'
app = require 'application'

###
    The UsersTableEntry shows a row in the user table.

    @author Sebastian Sachtleben
###
class UsersTableEntry extends TableEntryView

	editEntry: (event) ->
		console.log "Edit #{@model.id}"
		app.navigate "admin/user/#{@model.id}", true 
		
	deleteEntry: (event) ->
		console.log "Delete entry #{@model.id}"

return UsersTableEntry