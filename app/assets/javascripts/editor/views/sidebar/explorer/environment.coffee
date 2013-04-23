BaseView = require 'views/baseView'
templates = require 'templates'

class EnvironmentExplorer extends BaseView

	entity: 'environmentsStatic'

	template: templates.get 'sidebar/explorer/environment.tmpl'

return EnvironmentExplorer