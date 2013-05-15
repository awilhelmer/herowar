BaseController = require 'controllers/baseController'
Variables = require 'variables'
Input = require 'core/input'
Scene = require 'core/scene'
Engine = require 'engine'
db = require 'database'

class ApplicationController extends BaseController

	views:
		'views/viewport' : ''

	viewports: [
		domId: '#viewport',
		type: Variables.VIEWPORT_TYPE_RTS,
		camera:
			position: [ 0, 350, 0 ],
			rotation: [ -Math.PI/2, 0, 0 ]
	]

	initialize: (options) ->
		super options
		@data = db.data()
		@initViewports()	
		@initEngine()
		@initCore()
	
	initViewports: ->
		for viewport in @viewports
			@initViewport viewport	
	
	initViewport: (viewport) ->
		viewports = db.get 'ui/viewports'
		viewports.add viewport
	
	initEngine: ->
		@engine = new Engine
			container : $ '#viewport'
			data : @data
		@engine.init()
	
	initCore: ->
		@input = new Input @
		@scene = new Scene @

return ApplicationController