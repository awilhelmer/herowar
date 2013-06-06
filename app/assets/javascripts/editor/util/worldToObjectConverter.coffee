db = require 'database'

worldToObjectConverter =
	
	convert: ->
		world = db.get 'world'
		obj = _.clone world.attributes
		@handleMaterials()	
		@fillMaterialArray obj
		@convertGeometryVertices obj
		@convertGeometryUvs obj
		@convertGeometryFaces obj
		@convertThreeGeometry obj
		@addObjects obj
		@addPaths obj
		@addWaves obj
		console.log obj
		return obj

	handleMaterials: ->
		EditorEventbus = require 'editorEventbus'
		EditorEventbus.dispatch 'handleWorldMaterials'

	fillMaterialArray: (obj) ->
		materials = []
		col = db.get 'materials'
		for mat in col.models
			saveMat = _.clone mat.attributes
			saveMat.id = saveMat.materialId
			materials.push saveMat
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
			hasFaceVertexUvs = obj.terrain.geometry.uvs and obj.terrain.geometry.uvs.length > 0
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
			faces.push obj.terrain.geometry.uvs[0][i] for i in [0..nVertices-1] if hasFaceVertexUvs
		obj.terrain.geometry.faces = faces
	
	convertGeometryUvs: (obj) ->
		newUvs = []
		for uvs in obj.terrain.geometry.uvs
			for uvs2 in uvs
				for uvs3 in uvs2
					newUvs.push uvs3.x
					newUvs.push uvs3.y
		obj.terrain.geometry.uvs = [ newUvs ]
	
	convertUvs: (uvs) ->
		for uvs in obj.terrain.geometry.uvs
			if _.isArray uvs
				@convertUvs uvs
			else
				
			
	
	setBit: (value, position, bool) ->
		if bool then value | (1 << position) else value & ~(1 << position)

	convertThreeGeometry: (obj) ->
		geometry = obj.terrain.geometry
		unless geometry instanceof THREE.Geometry
			geometry.matIdMapper = geometry.userData.matIdMapper
			return
		geometry =
			id: geometry.userData.id
			vertices: geometry.vertices
			faces: geometry.faces
			morphTargets: geometry.morphTargets
			morphColors: geometry.morphColors
			normals:	geometry.normals
			colors: geometry.colors
			uvs:	geometry.uvs
			scale: geometry.scale
			type:	geometry.userData.type
			metadata: geometry.userData.metadata
			matIdMapper: geometry.userData.matIdMapper
			version: geometry.userData.version
			cdate: geometry.userData.cdate
		obj.terrain.geometry = geometry
	
	
	addObjects: (obj) ->
		obj.staticGeometries = null
		#delete obj.staticGeometries
		geometries = db.get 'geometries'
		environmentsStatic = db.get 'environmentsStatic'
		objects = []
		for model in environmentsStatic.models
			staticObj = model.attributes
			staticObj.geometry = id: staticObj.dbId
			convertedMesh = _.pick staticObj, 'geometry', 'position', 'rotation', 'scale' , 'name'
			convertedMesh.id = staticObj.meshId
			objects.push convertedMesh
		obj.objects = objects

	addPaths: (obj) ->
		paths = db.get 'db/paths'
		waypoints = db.get 'db/waypoints'
		obj.paths = []
		for currentPath in paths.models
			currentWaypoints = waypoints.where path : currentPath.get 'id'
			path = _.pick currentPath.attributes, 'id', 'name'
			path.id = currentPath.attributes.dbId
			path.waypoints = []
			for currentWaypoint in currentWaypoints
				waypoint = _.pick currentWaypoint.attributes, 'id', 'name', 'position'
				waypoint.id = currentWaypoint.attributes.dbId
				path.waypoints.push waypoint
			obj.paths.push path

	addWaves: (obj) ->
		waves = db.get 'db/waves'
		obj.waves = []
		for currentWave in waves.models
			wave = _.pick currentWave.attributes, 'id', 'name', 'prepareTime', 'waveTime', 'quantity'
			wave.id = currentWave.attributes.dbId
			if currentWave.attributes.path
				wave.pathId = currentWave.attributes.path
			wave.unitIds = [] 
			if currentWave.attributes.unit		
				wave.unitIds.push currentWave.attributes.unit
			obj.waves.push wave

return worldToObjectConverter