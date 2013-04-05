BasePanel = require 'ui/panel/basePanel'
	
class TerrainPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-terrain'

	initialize: ->
		console.log 'Initialize editor terrain properties'
		super()

	bindEvents: ->
		@$container.on 'keyup', 'input[name="width"],input[name="height"]', @changeTerrainSize
		@$container.on 'click', 'input[name="wireframe"]', @changeWireframe
		
	changeTerrainSize: =>
		width = @$container.find('input[name="width"]').val()
		height = @$container.find('input[name="height"]').val()
		@app.scenegraph().setMap @app.scenegraph().createDefaultMap width, height
		@app.render()

	changeWireframe: (event) =>
		map = @app.scenegraph().getMap()
		if @hasWireframe map
			@removeWireframe map
		else
			@addWireframe map, 0xFFFFFF
		@app.render()

	hasWireframe: (obj) ->
		found = false
		for mesh in obj.children
			found = true if mesh.material.wireframe
		found

	addWireframe: (obj, color) ->
		@app.scenegraph().scene.remove(obj)
		obj.add new THREE.Mesh obj.children[0].geometry, new THREE.MeshBasicMaterial(color: color, wireframe: true)
		@app.scenegraph().scene.add(obj)

	removeWireframe: (obj) ->
		@app.scenegraph().scene.remove(obj)
		foundMesh = true
		while foundMesh
			foundMesh = false
			meshId = 0
			for mesh in obj.children
				if mesh.material.wireframe
					foundMesh = true
					break;
				meshId++
			if foundMesh
				mesh = obj.children[meshId]
				mesh.geometry.dispose()
				mesh.material.dispose()
				@app.scenegraph().scene.remove(mesh)
				# TODO: remove textures too
				obj.children.splice meshId, 1
		@app.scenegraph().scene.add(obj)

return TerrainPropertiesPanel