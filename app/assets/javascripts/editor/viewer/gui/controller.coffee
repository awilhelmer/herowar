ModelsGUI = require 'viewer/gui/models'
RootGUI = require 'viewer/gui/root'
db = require 'database'

__run__ = false
__root__ = null
__models__ = [ 'rotation', 'scale', 'animations', 'effects', 'debug' ]

GUIController = 

	start: ->
		unless __run__
			@reset()
			@_bindEvents()
			__run__ = true
		return

	reset: ->
		if __root__
			__root__.root.destroy()
			delete __root__
		__root__ = new RootGUI()
		__root__.add new ModelsGUI()

	update: (obj) ->
		@reset()
		for mod in __models__
			Model = require "viewer/gui/#{mod}"
			current = new Model obj
			__root__.add current if current.isAllowed()
		return;

	_bindEvents: ->
		@viewer = db.get 'viewer'
		@viewer.on 'fetched:data', @update, @
		return

return GUIController