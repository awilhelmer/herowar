BaseModelView = require 'views/baseModelView'
templates = require 'templates'

class VideoSettingsView extends BaseModelView

	template: templates.get 'settings/video.tmpl'
	
	entity: 'db/settings'

	getTemplateData: ->
		json = super()
		console.log 'Render video settings:', json
		return json;

return VideoSettingsView