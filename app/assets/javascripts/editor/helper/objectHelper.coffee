class ObjectHelper
	
	constructor: (@editor) ->

	hasWireframe: (obj) ->
		found = false
		for mesh in obj.children
			found = true if mesh.name is 'wireframe'
			
		found

	addWireframe: (obj, color) ->
		#TODO hard coded children access ...
		materials = []		
		materials.push new THREE.MeshBasicMaterial(color: color, wireframe: true)
		mesh = new THREE.Mesh obj.children[0].geometry.clone(), new THREE.MeshFaceMaterial materials
		mesh.rotation.copy obj.children[0].rotation.clone()
		mesh.name = 'wireframe'
		obj.add mesh

	removeWireframe: (obj) ->
		foundMesh = true
		while foundMesh
			foundMesh = false
			meshId = 0
			for mesh in obj.children
				if mesh.name is 'wireframe'
					foundMesh = true
					break;
				meshId++
			if foundMesh
				mesh = obj.children[meshId]	
				obj.remove mesh
		null

	changeWireframeColor: (obj, color) ->
			for mesh in obj.children
				if mesh.name is 'wireframe'	and mesh.material.materials
					mesh.material.materials[0].color.set color if mesh.material.materials[0].wireframe

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