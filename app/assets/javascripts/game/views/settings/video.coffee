BaseModelView = require 'views/baseModelView'
templates = require 'templates'

class VideoSettingsView extends BaseModelView

	template: templates.get 'settings/video.tmpl'
	
	entity: 'db/settings'

	render: ->
		console.log 'Render video settings:', @getTemplateData()
		super()

return VideoSettingsView