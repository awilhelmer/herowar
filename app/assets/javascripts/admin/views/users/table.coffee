TableView = require 'views/tableView'
templates = require 'templates'

###
    The UsersListTable shows a list of users

    @author Sebastian Sachtleben
###
class UsersListTable extends TableView

	entity: 'api/users'
	
	template: templates.get 'users/table.tmpl'

return UsersListTable