TableView = require 'views/tableView'

###
    The UsersTable shows a list of users

    @author Sebastian Sachtleben
###
class UsersTable extends TableView

	entity: 'api/users'
	
	tableEntity: 'db/users'
	
	fields:
		'#'					: 'id'
		'Username' 	: 'username'
		'Email'			: 'email'
		
	entryView: 'views/users/tableEntry'

return UsersTable