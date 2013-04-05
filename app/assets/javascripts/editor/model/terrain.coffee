class Terrain
	
	name: 'Terrain'

	materials: [ new THREE.MeshBasicMaterial color: 0x006600 ]

	update: (width, height, smoothness, zScale) ->
		@model = generateTerrain width, height, smoothness
		@updateZ zScale

	updateZ: (zScale) ->
		width = @model[0].length - 1
		height = @model.length - 1
		terrain = []
		for i in [0..height]
			row = []
			for j in [0..width]
				row.push @model[i][j] * zScale
			terrain.push row
		@getTerrainMesh terrain

	getTerrainMesh: (terrain) ->
		terrainWidth  = terrain[0].length - 1
		terrainHeight = terrain.length - 1
		
		segLength = @getOptimalSegLength terrainWidth, terrainHeight
		
		width = (terrain[0].length - 1) * segLength
		height = (terrain.length - 1) * segLength
		
		console.log width
		console.log height
		
		obj = THREE.SceneUtils.createMultiMaterialObject new THREE.PlaneGeometry(width, height, terrainHeight, terrainHeight), @materials
		obj.name = @name
		for mesh in obj.children
			for i in [0..terrainHeight]
				for j in [0..terrainHeight]
					mesh.geometry.vertices[i * terrain.length + j].z = terrain[i][j]
		obj

	getOptimalSegLength: (width, height) ->
		~~(640 / Math.max(width, height))

return Terrain