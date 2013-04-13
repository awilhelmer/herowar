class ObjectHelper
	
	constructor: (@editor) ->

	hasWireframe: (obj) ->
		found = false
		for mesh in obj.children
			found = true if mesh.material.wireframe
		found

	addWireframe: (obj, color) ->
		@editor.engine.scenegraph.scene.remove(obj)
		#TODO hard coded children access ...
		materials = []		
		materials.push new THREE.MeshBasicMaterial color: color, wireframe: true
		mesh = new THREE.Mesh obj.children[0].geometry, new THREE.MeshFaceMaterial materials
		mesh.rotation.copy obj.children[0].rotation
		mesh.name = 'wireframe'
		obj.add mesh
		@editor.engine.scenegraph.scene.add obj

	removeWireframe: (obj) ->
		@editor.engine.scenegraph.scene.remove obj
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
				@editor.engine.scenegraph.scene.remove mesh
				# TODO: remove textures too
				obj.children.splice meshId, 1
		@editor.engine.scenegraph.scene.add obj

	changeWireframeColor: (obj, color) ->
			for mesh in obj.children
				mesh.material.color.set color if mesh.material.wireframe

	getBaseObject: (obj) ->	
		if obj
			while !_.isUndefined obj.parent
					obj = obj.parent
					break if obj.parent instanceof THREE.Scene
		obj
		
	isTerrain: (obj) ->
		if _.isUndefined obj or _.isUndefined @editor.engine.scenegraph.map then return false
		obj = @getBaseObject obj unless obj.parent instanceof THREE.Scene
		@editor.engine.scenegraph.map.id is obj.id

return ObjectHelper