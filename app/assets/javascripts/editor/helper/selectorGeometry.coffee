EditorEventbus = require 'editorEventbus'

class SelectorGeometry
	
	constructor: (@editor) ->
		@bindEvents()

	bindEvents: ->
		EditorEventbus.listSelectItem.add @onSelectItem

	onSelectItem: (name, value) ->
		console.log "SelectorGeometry #{name}, #{value}"

return SelectorGeometry