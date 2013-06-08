BaseController = require 'controllers/baseController'
app = require 'application'
popup = require 'popup'

class BasePopupController extends BaseController
	
	initialize: (options) ->
		return @openPopup() if isInternalNavigate()
		super options

	openPopup: ->
		for viewName of @views
			return popup.open view: @_createView viewName

isInternalNavigate = ->
	_.size(app.views) > 0

return BasePopupController