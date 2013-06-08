BaseView = require 'views/baseView'
templates = require 'templates'

class VideoSettingsView extends BaseView

	template: templates.get 'settings/video.tmpl'

return VideoSettingsView