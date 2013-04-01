AdminTableView = require 'views/adminTableView'
templates = require 'templates'
app = require 'application'

###
    The UsersList shows a list of users

    @author Sebastian Sachtleben
###
class UsersList extends AdminTableView

	entity: 'api/users'

	id: 'users-list'
	
	template: templates.get 'users/list.tmpl'

return UsersList