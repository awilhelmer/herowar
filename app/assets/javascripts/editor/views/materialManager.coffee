BaseView = require 'views/baseView'
templates = require 'templates'

class MaterialManager extends BaseView

	id: 'materialManager'

	template: templates.get 'materialManager.tmpl'

return MaterialManager