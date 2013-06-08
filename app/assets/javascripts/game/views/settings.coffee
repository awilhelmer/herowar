BasePopupView = require 'views/basePopupView'
templates = require 'templates'
db = require 'database'

class SettingsView extends BasePopupView

	id: 'settings'
		
	template: templates.get 'settings.tmpl'
	
	events:
		'click .apply'  : 'apply'
		'click .save'   : 'save'
		'click .cancel' : 'cancel'
		
	apply: (event) ->
		console.log 'Settings apply'
		settings = db.get 'db/settings'
		settings.save()

	save: (event) ->
		console.log 'Settings save'
		@apply()

	cancel: (event) ->
		console.log 'Settings cancel'

return SettingsView