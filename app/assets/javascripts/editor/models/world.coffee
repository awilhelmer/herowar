MaterialHelper = require 'helper/materialHelper'
Constants = require 'constants'
db = require 'database'

class World extends Backbone.Model
	
	initialize: (options) ->
		@materialHelper = new MaterialHelper()
		@initListener
		super options

	addTerrainMaterial: (materialId) ->
		materials = @get 'materials'
		materials = [] unless materials
		materials.push materialId
		@set 'materials', materials
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
		obj = @createTerrainMesh(new THREE.PlaneGeometry(@get('terrain').width, @get('terrain').height, segWidth, segHeight))
		for mesh in obj.children
			for i in [0..segHeight]
				for j in [0..segWidth]
					vector = mesh.geometry.vertices[i * segHeight + j]
					vector.z = terrain[i][j] if vector
		obj

	createTerrainMesh: (geometry) ->
		obj = new THREE.Object3D()
		obj.name = (@get 'name') + '_group'
		mesh = new THREE.Mesh geometry, new THREE.MeshFaceMaterial()
		material = db.get 'materials', Constants.MATERIAL_SELECTED
		@materialHelper.getThreeMaterialId mesh, id: material.get('id'), materialId: Constants.MATERIAL_SELECTED #TODO MAPPING ... 
		mesh.name = (@get 'name') + '_mesh'
		mesh.geometry.dynamic = true
		mesh.rotation.x = - Math.PI/2
		obj.add mesh
		obj

	#on saving parse MatGeoId Array in Geometry
	handleMaterials: (map) ->
		@materialHelper.handleGeometryForSave @attributes.terrain.geometry, map
		
		
	#onloading parse materials in geomtry
	loadMaterials: (data) ->
		materials = db.get 'materials'
		for mat in data.materials
			materials.add mat
		@materialHelper.loadGlobalMaterials data.terrain.geometry
	

return World