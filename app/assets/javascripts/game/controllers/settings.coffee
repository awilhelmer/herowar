BasePopupController = require 'controllers/basePopupController'
log = require 'util/logger'
db = require 'database'

class SettingsController extends BasePopupController
	
	views:
		'views/settings' : ''

	initialize: (options) ->
		log.info 'Initialize settings...'
		super options
		settings = db.get 'db/settings'
		settings.fetch()

return SettingsController