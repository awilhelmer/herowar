RotationGUI = require 'viewer/gui/rotation'
ModelsGUI = require 'viewer/gui/models'
RootGUI = require 'viewer/gui/root'
db = require 'database'

__run__ = false
__root__ = null

GUIController = 

	start: ->
		unless __run__
			__root__ = new RootGUI()
			__root__.add new ModelsGUI()
			@_bindEvents()
			__run__ = true

	update: (obj) ->
		__root__.add new RotationGUI obj

	_bindEvents: ->
		console.log 'Bind events on gui controller'
		@viewer = db.get 'viewer'
		@viewer.on 'fetched:data', @update, @

return GUIController