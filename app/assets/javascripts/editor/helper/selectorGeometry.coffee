EditorEventbus = require 'editorEventbus'
MaterialHelper = require 'helper/materialHelper'

class SelectorGeometry
	
	constructor: (@editor) ->
		@id = -1
		@loader = new THREE.JSONLoader()
		@materialHelper = new MaterialHelper()
		@bindEvents()

	bindEvents: ->
		EditorEventbus.listSelectItem.add @onSelectItem

	onSelectItem: (name, value) =>
		console.log "SelectorGeometry #{name}, #{value}"
		if name is 'sidebar-environment-geometries' and @id isnt value
			@id = value
			@loader.load "/api/game/geometry/env/#{@id}", @onLoadGeometry, 'assets/images/game/textures'
			
	onLoadGeometry: (geometry, materials) =>
		console.log "Successfully loaded geometry with id #{@id}"
		mesh = new THREE.Mesh geometry
		if materials
			mesh.material = new THREE.MeshFaceMaterial materials
		@editor.engine.scenegraph.scene.add mesh
		@editor.engine.render()
		
return SelectorGeometry