BasePopupView = require 'views/basePopupView'
templates = require 'templates'

class PreferencesView extends BasePopupView

	id: 'preferences'
		
	template: templates.get 'preferences.tmpl'

return PreferencesView