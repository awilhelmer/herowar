EditController = require 'controllers/editController'

class UsersEditController extends EditController

	views:
		'views/header'			: ''
		'views/users/edit' 	: 'getEditOptions'

	collection: 'db/users'

return UsersEditController