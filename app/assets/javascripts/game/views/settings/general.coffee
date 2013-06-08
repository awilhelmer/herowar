BaseView = require 'views/baseView'
templates = require 'templates'

class GeneralSettingsView extends BaseView

	template: templates.get 'settings/general.tmpl'

	entity: 'db/settings'

return GeneralSettingsView