class ObjectHelper
	
	constructor: (@editor) ->

	hasWireframe: (obj) ->
		found = false
		for mesh in obj.children
			found = true if mesh.material.wireframe
		found

	addWireframe: (obj, color) ->
		@editor.scenegraph().scene.remove(obj)
		mesh = new THREE.Mesh obj.children[0].geometry, new THREE.MeshBasicMaterial(color: color, wireframe: true)
		mesh.rotation.copy obj.children[0].rotation
		obj.add mesh
		@editor.scenegraph().scene.add(obj)

	removeWireframe: (obj) ->
		@editor.scenegraph().scene.remove(obj)
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
				@editor.scenegraph().scene.remove(mesh)
				# TODO: remove textures too
				obj.children.splice meshId, 1
		@editor.scenegraph().scene.add(obj)

	changeWireframeColor: (obj, color) ->
			for mesh in obj.children
				mesh.material.color.set color if mesh.material.wireframe	

return ObjectHelper