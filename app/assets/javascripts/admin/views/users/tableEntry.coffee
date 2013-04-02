TableEntryView = require 'views/tableEntryView'
app = require 'application'
db = require 'database'

###
    The UsersTableEntry shows a row in the user table.

    @author Sebastian Sachtleben
###
class UserTableEntry extends TableEntryView

	entityType: 'user'
		
	deleteField: 'username'
		
	deleteEntry: (event) ->
		event?.preventDefault()
		me = db.get 'ui/me'
		if me.id == @model.id
			$.gritter.add
				title: 'Error',
				text: 'You can\'t delete yourself...'
		else
			super event

return UserTableEntry