MaterialHelper = require 'helper/materialHelper'
Constants = require 'constants'
db = require 'database'

class World extends Backbone.Model
	
	initialize: (options) ->
		@materialHelper = new MaterialHelper()
		@initListener
		super options

	addTerrainMaterial: (idMapper) ->
		#@loadMaterials
		@trigger 'change'
		@trigger 'change:materials'

	terrainUpdate: ->
		segWidth = Math.round @get('terrain').width / 10
		segHeight = Math.round @get('terrain').height / 10
		model = generateTerrain segWidth, segHeight, @get('terrain').smoothness
		zScale = @get('terrain').zScale
		terrain = []
		for i in [0..segHeight]
			row = []
			for j in [0..segWidth]
				row.push model[i][j] * zScale
			terrain.push row
		@buildRandomTerrainMesh terrain, segWidth, segHeight

	getTerrainMeshFromGeometry: ->
		@createTerrainMesh @get('terrain').geometry

	buildRandomTerrainMesh: (terrain, segWidth, segHeight) ->		
		matIdMapper = [] 
		col = db.get 'materials'
		for mat in col.models
			matIdMapper.push id:mat.attributes.id, materialId:mat.attributes.materialId
		geo = new THREE.PlaneGeometry(@get('terrain').width, @get('terrain').height, segWidth, segHeight)
		geo.userData = matIdMapper: matIdMapper
		obj = @createTerrainMesh(geo)
		for mesh in obj.children
			for i in [0..segHeight]
				for j in [0..segWidth]
					vector = mesh.geometry.vertices[i * segHeight + j]
					vector.z = terrain[i][j] if vector
		obj

	createTerrainMesh: (geometry) ->
		obj = new THREE.Object3D()
		obj.name = (@get 'name') + '_group'
		mesh = new THREE.Mesh geometry, new THREE.MeshFaceMaterial([])
		mesh.name = (@get 'name') + '_mesh'
		mesh.geometry.dynamic = true
		mesh.rotation.x = - Math.PI/2
		if geometry.userData and geometry.userData.matIdMapper
			for mat in geometry.userData.matIdMapper
				@materialHelper.getThreeMaterialId mesh, (id: mat.id, materialId: mat.materialId)
		obj.add mesh
		obj

	#on saving parse MatGeoId Array in Geometry
	handleMaterials: (map) ->
		@materialHelper.handleGeometryForSave @attributes.terrain.geometry, map
		
		
	#onloading parse materials in geometry
	loadMaterials: (materials) ->
		col = db.get 'materials'
		materials = [] unless materials
		i = 1
		for mat in materials
			mat.id = i
			col.add mat
			i++
		#@materialHelper.loadGlobalMaterials data.terrain.geometry
	

return World