BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Footer shows some links to infomation views and the copyright.

    @author Sebastian Sachtleben
###
class Footer extends BaseView

	className: 'footer'
	
	template: templates.get 'footer.tmpl'
	
return Footer