EditorEventbus = require 'editorEventbus'

class SelectorGeometry
	
	constructor: (@editor) ->
		@id = -1
		@loader = new THREE.JSONLoader()
		@bindEvents()

	bindEvents: ->
		EditorEventbus.listSelectItem.add @onSelectItem

	onSelectItem: (name, value) =>
		console.log "SelectorGeometry #{name}, #{value}"
		if name is 'sidebar-environment-geometries' and @id isnt value
			@id = value
			@loader.load "/api/game/geometry/#{@id}", @onLoadGeometry
			
	onLoadGeometry: (geometry, materials) =>
		console.log "Successfully loaded geometry with id #{@id}"

return SelectorGeometry