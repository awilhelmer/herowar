BasePanel = require 'ui/panel/basePanel'
	
class ObjectPropertiesPanel extends BasePanel

	constructor: (@editor) ->
		super @editor, 'sidebar-properties-world'

	initialize: ->
		console.log 'Initialize editor object properties'
		super()

	addSelectionWireframe: ->

	removeSelectionWireframe: ->

return ObjectPropertiesPanel