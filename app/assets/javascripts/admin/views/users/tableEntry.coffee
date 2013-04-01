TableEntryView = require 'views/tableEntryView'
templates = require 'templates'
app = require 'application'

###
    The UsersListTableEntry shows a single entry in users list.

    @author Sebastian Sachtleben
###
class UsersListTableEntry extends TableEntryView

	entity: 'db/users'
	
	template: templates.get 'users/tableEntry.tmpl'

return UsersListTableEntry