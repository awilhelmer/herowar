BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

class SettingsView extends BaseView

	className: 'settings'

	events:
		'click' : 'invoke'
		
	invoke: (event) ->
		Backbone.history.loadUrl 'game/settings'

return SettingsView