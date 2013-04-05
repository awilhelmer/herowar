class ObjectHelper
	
	constructor: (@app) ->

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

	changeWireframeColor: (obj, color) ->
			for mesh in obj.children
				mesh.material.color.set color if mesh.material.wireframe	

return ObjectHelper