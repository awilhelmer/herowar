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
		segWidth = Math.round(@get('terrain').width / 10)
		segHeight = Math.round(@get('terrain').height / 10)
		model = generateTerrain segWidth, segHeight, @get('terrain').smoothness
		zScale = @get('terrain').zScale
		terrain = []
		for i in [0..segHeight]
			row = []
			for j in [0..segWidth]
				row.push model[i][j] * zScale
			terrain.push row
		@getTerrainMesh terrain, segWidth, segHeight

	getTerrainMesh: (terrain, segWidth, segHeight) ->		
		obj = new THREE.Object3D()
		obj.name = (@get 'name') + '_group'
		mesh = new THREE.Mesh new THREE.PlaneGeometry(@get('terrain').width, @get('terrain').height, segWidth, segHeight), new THREE.MeshFaceMaterial()
		material = db.get 'materials', Constants.MATERIAL_SELECTED
		@materialHelper.getThreeMaterialId mesh, id: material.get('id'), materialId: Constants.MATERIAL_SELECTED #TODO MAPPING ... 
		mesh.name = (@get 'name') + '_mesh'
		mesh.geometry.dynamic = true
		obj.add mesh
		for mesh in obj.children
			mesh.rotation.x = - Math.PI/2
			for i in [0..segHeight]
				for j in [0..segWidth]
					vector = mesh.geometry.vertices[i * segHeight + j]
					vector.z = terrain[i][j] if vector
		obj
	
	handleMaterials: (map) ->
		#Iterate geometries and its materials
		#search the array index in world.materials with condition THREE.Material.name (withoud MATID) = material.id
		#push the index it in worldData.terrein.geometry.materialIndexes array
		
	
return World