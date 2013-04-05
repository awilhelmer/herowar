BasePanel = require 'ui/panel/basePanel'
	
class WorldPropertiesPanel extends BasePanel

	constructor: (@editor) ->
		super @editor, 'sidebar-properties-world'

	initialize: ->
		console.log 'Initialize editor world properties'
		@editor.scenegraph().addSkybox 'default'
		@editor.render()
		super()

	addSelectionWireframe: ->

	removeSelectionWireframe: ->

return WorldPropertiesPanel