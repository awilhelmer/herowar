BasePanel = require 'ui/panel/basePanel'
	
class WorldPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-world'

	initialize: ->
		console.log 'Initialize editor world properties'
		super()

	addSelectionWireframe: ->

	removeSelectionWireframe: ->

return WorldPropertiesPanel