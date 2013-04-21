BaseView = require 'views/baseView'
templates = require 'templates'

class EnvironmentCategories extends BaseView
	
	template: templates.get 'sidebar/environmentCategories.tmpl'

return EnvironmentCategories