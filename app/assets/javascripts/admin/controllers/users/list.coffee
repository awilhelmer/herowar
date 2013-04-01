BaseController = require 'controllers/baseController'

###
    The UsersListController shows a list of all users.

    @author Sebastian Sachtleben
###
class UsersListController extends BaseController

    views:
        'views/header'			: ''
        'views/users/list' 	: ''

return UsersListController