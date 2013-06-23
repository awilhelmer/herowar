materialHelper = require 'helper/materialHelper'
Environment = require 'models/environment'
Material = require 'models/material'
Waypoint = require 'models/db/waypoint'
Texture = require 'models/texture'
Path = require 'models/db/path'
scenegraph = require 'scenegraph'
Wave = require 'models/db/wave'
events = require 'events'
engine = require 'engine'
db = require 'database'

class Scene
	
	constructor: ->
		@world = db.get 'world'
		@initialize()
		@reset()
		@createTextures()
		@createStaticObjects() if @world.has('staticGeometries') and @world.get('staticGeometries').length isnt 0
		@createPaths()
		@createWaves()
		events.trigger 'scene:created'
		
	# Override for custom initialize
	initialize: ->
		
	reset: =>
		@createTerrainMaterial()
		skybox = @world.get 'skybox'
		scenegraph.addSkybox skybox if skybox
		@buildTerrain()

	createTerrainMaterial: ->
		col = db.get 'materials'
		if col.models.length == 0
			mat = new Material()
			mat.set 
				'id'					: 1
				'materialId' 	: -1
				'name' 				: 'Terrain'
				'color' 			: '#006600'
				'transparent' : false
				'opacity'			: 1
				'map'					: undefined
			col.add mat
			@world.addTerrainMaterial id : mat.id, materialId: mat.merialId
	
	buildTerrain: (map) =>
		if !map and @world.get('terrain').geometry instanceof THREE.Geometry
			map = @world.getTerrainMeshFromGeometry()
			map.children[0].geometry.computeBoundingBox()
			map.children[0].geometry.computeBoundingSphere()
			map.children[0].geometry.computeFaceNormals()
			map.children[0].geometry.computeVertexNormals()
			scenegraph.setMap map
			events.trigger 'scene:terrain:build', map
		throw 'Map is undefined' if !map
		map

	createTextures: ->
		@textures = db.get 'textures'
		nextTextureId = 1
		@textures.add @createTexture nextTextureId++, 'Blank', '', undefined
		@textures.add @createTexture nextTextureId++, key, val.sourceFile, val for key, val of db.data().textures

	createTexture: (id, name, sourceFile, threeTexture) ->
		texture = new Texture()
		texture.set
			'id'						: id
			'name' 					: name
			'sourceFile'		: sourceFile
			'threeTexture'	: threeTexture
		texture

	createStaticObjects: ->
		mesh = null
		for instance in @world.attributes.objects
			unless scenegraph.hasStaticObject instance.geoId 
				for staticMesh in @world.attributes.staticGeometries
					if staticMesh.userData.id is instance.geoId
						mesh = staticMesh
						mesh.name = instance.name
						break
			else
				mesh = scenegraph.staticObjects[instance.geoId][0]
				mesh = materialHelper.createMesh mesh.geometry, mesh.material.materials, instance.name, id:mesh.userData.dbId
			#add position to mesh ... 
			if mesh
				mesh.position = new THREE.Vector3 instance.position.x,instance.position.y,instance.position.z
				mesh.rotation = new THREE.Vector3 instance.rotation.x,instance.rotation.y,instance.rotation.z
				mesh.scale = new THREE.Vector3 instance.scale.x,instance.scale.y,instance.scale.z
				mesh.userData.meshId = instance.id
				scenegraph.addStaticObject mesh, mesh.userData.dbId
				id = scenegraph.getNextId()
				environmentsStatic = db.get 'environmentsStatic'
				environmentsStatic.add @createModelFromMesh id, mesh, mesh.name
		engine.render()
		null

	createPaths: ->
		paths = db.get 'db/paths'
		waypoints = db.get 'db/waypoints'
		if @world.attributes.paths
			wayId = 1
			pathId = 1
			for path in @world.attributes.paths
				pathDbId = path.id
				path.id = pathId++
				pathModel = new Path()
				pathModel.set path
				pathModel.attributes.dbId = pathDbId
				paths.add pathModel
				for waypoint in path.waypoints
					waypointDbId = waypoint.id
					waypoint.id = wayId++
					waypointModel = new Waypoint()
					waypointModel.set waypoint
					waypointModel.attributes.dbId = waypointDbId
					waypointModel.attributes.path = pathModel.attributes.id
					waypoints.add waypointModel
			@afterCreatingPaths wayId, pathId
	 	null

	afterCreatingPaths: (wayId, pathId) ->

	createWaves: ->
		waves = db.get 'db/waves'
		if @world.attributes.waves
			waveId = 1
			for wave in @world.attributes.waves
				waveDbId = wave.id
				wave.id = waveId++
				waveModel = new Wave()
				waveModel.set wave
				waveModel.attributes.dbId = waveDbId
				waveModel.attributes.path = wave.pathId
				if wave.unitIds and wave.unitIds.length > 0
					waveModel.attributes.unit =	wave.unitIds[0]
				waves.add waveModel
			@afterCreatingWaves waveId
		null
	
	afterCreatingWaves: (waveId) ->

	#TODO central static code please!
	createModelFromMesh: (id, mesh, name) ->
		env = new Environment()
		env.set
			id 				: id
			dbId			: mesh.userData.dbId
			meshId		:	mesh.userData.meshId
			listIndex	: mesh.userData.listIndex
			name 			: name
			position	:
				x				: mesh.position.x
				y				: mesh.position.y
				z				: mesh.position.z
			rotation	:
				x				: mesh.rotation.x
				y				: mesh.rotation.y
				z				: mesh.rotation.z
			scale			:
				x				: mesh.scale.x
				y				: mesh.scale.y
				z				: mesh.scale.z
		env

return Scene