AuthView = require 'views/authView'

###
    The AdminAuthView overrides the redirectTo path of the default AuthView.

    @author Sebastian Sachtleben
###
class AdminAuthView extends AuthView

	redirectTo: 'admin/login'

return AdminAuthView