BaseView = require 'views/baseView'
templates = require 'templates'
popup = require 'popup'
db = require 'database'

class SettingsView extends BaseView

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
		popup.close()

	cancel: (event) ->
		console.log 'Settings cancel'
		settings = db.get 'db/settings'
		settings.fetch()
		popup.close()

return SettingsView