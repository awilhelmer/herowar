AddTower = require 'tools/addTower'
Tools = require 'core/tools'

class EditorTools extends Tools
	
	createTools: ->
		@addTower = new AddTower @app, @intersectHelper

return EditorTools