BaseView = require 'views/baseView'
templates = require 'templates'

class SoundSettingsView extends BaseView

	template: templates.get 'settings/sound.tmpl'

	entity: 'db/settings'

return SoundSettingsView