AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

###
    The UsersList shows a list of users.

    @author Sebastian Sachtleben
###
class UsersList extends AdminAuthView

	entity: 'api/users'

	id: 'users-list'
	
	template: templates.get 'users/list.tmpl'

return UsersList