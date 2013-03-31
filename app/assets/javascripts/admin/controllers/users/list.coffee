BaseController = require 'controllers/baseController'

###
    The users list controller shows a list of all users.

    @author Sebastian Sachtleben
###
class UsersListController extends BaseController

    views:
        'views/header'			: ''
        'views/users/list' 	: ''

return UsersListController