BasePanel = require 'ui/panel/basePanel'
	
class ObjectPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-world'

	initialize: ->
		console.log 'Initialize editor object properties'
		super()

return ObjectPropertiesPanel