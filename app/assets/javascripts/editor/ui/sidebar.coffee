Toolbar = require 'ui/toolbar'
Scenegraph = require 'ui/panel/scenegraph'
	
class Sidebar extends Toolbar

	constructor: (@editor) ->
		super @editor

	init: ->
		console.log 'Initialize sidebar'
		@scenepgraph = new Scenegraph @editor
		@scenepgraph.init()

return Sidebar