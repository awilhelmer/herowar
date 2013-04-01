AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

###
    The UsersList shows a list of users

    @author Sebastian Sachtleben
###
class UserShow extends AdminAuthView

	entity: 'db/user'

	id: 'user-show'
	
	template: templates.get 'users/show.tmpl'

return UserShow