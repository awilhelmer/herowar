db = require 'database'

worldToObjectConverter =
	
	convert: ->
		world = db.get 'world'
		obj = _.clone world.attributes
		@handleMaterials()
		@fillMaterialArray obj
		@convertGeometryVertices obj
		@convertGeometryFaces obj
		# TODO: fill geometry meta data...
		@addObjects obj
		return obj

	handleMaterials: ->
		EditorEventbus = require 'editorEventbus'
		EditorEventbus.handleWorldMaterials.dispatch()

	fillMaterialArray: (obj) ->
		materials = []
		for index in obj.materials
			materials.push db.get 'materials', index 
		obj.materials = materials

	convertGeometryVertices: (obj) ->
		vertices = []
		for vector in obj.terrain.geometry.vertices
			vertices.push vector.x
			vertices.push vector.y
			vertices.push vector.z
		obj.terrain.geometry.vertices = vertices

	convertGeometryFaces: (obj) ->
		faces = []
		for face in obj.terrain.geometry.faces
			isTriangle = face instanceof THREE.Face3
			if isTriangle then nVertices = 3 else nVertices = 4
				
			hasMaterial = true 							# for the moment OBJs without materials get default material
			hasFaceUvs = false 							# not supported in OBJ
			hasFaceVertexUvs = obj.terrain.geometry.uvs and obj.terrain.geometry.uvs.length >= nVertices
			hasFaceNormals = false 					# don't export any face normals (as they are computed in engine)
			hasFaceVertexNormals = false 		# not sure what this means...
			hasFaceColors = false						# not sure what this means...
			hasFaceVertexColors = false 		# not supported in OBJ
			
			faceType = 0
			faceType = @setBit faceType, 0, not isTriangle
			faceType = @setBit faceType, 1, hasMaterial
			faceType = @setBit faceType, 2, hasFaceUvs
			faceType = @setBit faceType, 3, hasFaceVertexUvs
			faceType = @setBit faceType, 4, hasFaceNormals
			faceType = @setBit faceType, 5, hasFaceVertexNormals
			faceType = @setBit faceType, 6, hasFaceColors
			faceType = @setBit faceType, 7, hasFaceVertexColors
			
			faces.push faceType
			faces.push val for key, val of _.pick face, 'a', 'b', 'c', 'd'
			faces.push face.materialIndex if hasMaterial
		obj.terrain.geometry.faces = faces
	
	setBit: (value, position, bool) ->
		if bool then value | (1 << position) else value & ~(1 << position)

	addObjects: (obj) ->
		geometries = db.get 'geometries'
		environmentsStatic = db.get 'environmentsStatic'
		objects = []
		for model in environmentsStatic.models
			json = model.toJSON()
			json.geometry = _.pick geometries.get(json.dbId).toJSON(), 'id'
			objects.push _.pick json, 'geometry', 'position', 'rotation', 'scale'
		obj.objects = objects

return worldToObjectConverter