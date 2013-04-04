Toolbar = require 'ui/toolbar'
Scenegraph = require 'ui/panel/scenegraph'
	
class Sidebar extends Toolbar

	constructor: (@app) ->
		super @app

	init: ->
		console.log 'Initialize sidebar'
		@scenepgraph = new Scenegraph @app
		@scenepgraph.init()

return Sidebar