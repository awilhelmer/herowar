BaseView = require 'views/baseView'
templates = require 'templates'

class VideoSettingsView extends BaseView

	template: templates.get 'settings/video.tmpl'
	
	entity: 'db/settings'

	render: ->
		console.log 'Render video settings:', @getTemplateData()
		super()

return VideoSettingsView