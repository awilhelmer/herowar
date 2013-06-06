ModelsGUI = require 'viewer/gui/models'
RootGUI = require 'viewer/gui/root'
db = require 'database'

__run__ = false
__root__ = null
__models__ = [ 'rotation', 'scale', 'animations', 'effects', 'debug' ]

GUIController = 

	start: ->
		unless __run__
			__root__ = new RootGUI()
			__root__.add new ModelsGUI()
			@_bindEvents()
			__run__ = true

	update: (obj) ->
		for mod in __models__
			Model = require "viewer/gui/#{mod}"
			current = new Model obj
			__root__.add current if current.isAllowed()

	_bindEvents: ->
		console.log 'Bind events on gui controller'
		@viewer = db.get 'viewer'
		@viewer.on 'fetched:data', @update, @

return GUIController