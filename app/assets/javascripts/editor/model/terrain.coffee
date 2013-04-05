class Terrain
	
	name: 'Terrain'

	materials: [ new THREE.MeshBasicMaterial color: 0x006600 ]

	update: (width, height, smoothness, zScale) ->
		@width = width
		@height = height
		@smoothness = smoothness
		@zScale = zScale
		@segWidth = Math.round(width / 10)
		@segHeight = Math.round(height / 10)
		@model = generateTerrain @segWidth, @segHeight, @smoothness
		@updateZ()

	updateZ: () ->
		terrain = []
		for i in [0..@segHeight]
			row = []
			for j in [0..@segWidth]
				row.push @model[i][j] * @zScale
			terrain.push row
		@getTerrainMesh terrain

	getTerrainMesh: (terrain) ->		
		obj = THREE.SceneUtils.createMultiMaterialObject new THREE.PlaneGeometry(@width, @height, @segWidth, @segHeight), @materials
		obj.name = @name
		for mesh in obj.children
			for i in [0..@segHeight]
				for j in [0..@segWidth]
					vector = mesh.geometry.vertices[i * @segHeight + j]
					vector.z = terrain[i][j] if vector
		obj

return Terrain