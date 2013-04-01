TableView = require 'views/tableView'

###
    The UsersTable shows a list of users.

    @author Sebastian Sachtleben
###
class UsersTable extends TableView

	entity: 'api/users'
	
	tableEntity: 'db/users'
	
	fields:
		'#'								: 'id'
		'Username' 				: 'username'
		'Email'						: 'email'
		'Active'					: 'active'
		'Last Login Date'	: 'lastLogin'
		'Creation Date'		: 'cdate'
		
	entryView: 'views/users/tableEntry'

	allowCreate: false

return UsersTable