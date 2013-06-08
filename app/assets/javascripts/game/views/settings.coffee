BasePopupView = require 'views/basePopupView'
templates = require 'templates'

class SettingsView extends BasePopupView

	id: 'settings'
		
	template: templates.get 'settings.tmpl'

return SettingsView