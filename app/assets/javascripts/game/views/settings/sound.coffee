BaseView = require 'views/baseView'
templates = require 'templates'

class SoundSettingsView extends BaseView

	template: templates.get 'settings/sound.tmpl'

return SoundSettingsView