ObjectHelper = require 'helper/objectHelper'
RandomPool = require 'helper/randomPool'
Environment = require 'models/environment'
materialHelper = require 'helper/materialHelper'
EditorEventbus = require 'editorEventbus'
Material = require 'models/material'
Texture = require 'models/texture'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class Scene

	constructor: (@editor) ->
		@isInitialized = false
		@initialize()
		@isInitialized = true
	
	initialize: ->
		@objectHelper = new ObjectHelper @editor
		@randomPool = new RandomPool()
		@randomPool.hook()
		@world = db.get 'world'
		@addEventListeners()
		@reset()		
		@createTextures()
		@createStaticObjects()

	addEventListeners: ->
		EditorEventbus.changeTerrain.add @changeTerrain
		EditorEventbus.changeTerrainWireframe.add @changeTerrainWireframe
		EditorEventbus.resetTerrainPool.add @resetTerrainPool
		EditorEventbus.handleWorldMaterials.add @handleMaterials
		EditorEventbus.removeStaticObject.add @removeStaticObject

	createTextures: ->
		@textures = db.get 'textures'
		nextTextureId = 1
		@textures.add @createTexture nextTextureId++, 'Blank', '', undefined
		@textures.add @createTexture nextTextureId++, key, val.sourceFile, val for key, val of @editor.data.textures

	createTexture: (id, name, sourceFile, threeTexture) ->
		texture = new Texture()
		texture.set
			'id'						: id
			'name' 					: name
			'sourceFile'		: sourceFile
			'threeTexture'	: threeTexture
		texture

	changeTerrain: (width, height, smoothness, zScale) =>
		width = parseInt width
		height = parseInt height
		smoothness = parseFloat smoothness
		zScale = parseInt zScale
		if @hasValidSize(width, height) and @hasChangedSize(width, height, smoothness, zScale)
			log.debug 'Terrain has valid changes'
			@world.get('terrain').width = width
			@world.get('terrain').height = height
			@world.get('terrain').smoothness = smoothness
			@world.get('terrain').zScale = zScale
			@buildTerrain()

	changeTerrainWireframe: (value) =>
		@world.get('terrain').wireframe = value

	resetTerrainPool: =>
		log.info 'Reseting terrain pool'
		@randomPool.reset()
		@buildTerrain()

	reset: =>
		log.info 'Reseting scene'
		@createTerrainMaterial()
		@editor.engine.scenegraph.addSkybox @world.get 'skybox'
		@resetTerrainPool()

	buildTerrain: =>
		log.info "Change terrain: size=#{@world.get('terrain').width}x#{@world.get('terrain').height} smoothness=#{@world.get('terrain').smoothness} zscale=#{@world.get('terrain').zScale}"
		@randomPool.seek 0
		if @world.get('terrain').geometry instanceof THREE.Geometry
			map = @world.getTerrainMeshFromGeometry()
		else
			map = @world.terrainUpdate()
		@world.get('terrain').geometry.faces = map.children[0].geometry.faces
		@world.get('terrain').geometry.vertices = map.children[0].geometry.vertices
		@objectHelper.addWireframe map, @getWireframeColor() if !@objectHelper.hasWireframe(map) or @world.get('terrain').wireframe
		@editor.engine.scenegraph.setMap map
		@editor.engine.render()

	getWireframeColor: =>
		if @isInitialized then 0xFFFF00 else 0xFFFFFF

	hasValidSize: (width, height) =>
		width >= Constants.TERRAIN_MIN_WIDTH and width <= Constants.TERRAIN_MAX_WIDTH and height >= Constants.TERRAIN_MIN_HEIGHT and height <= Constants.TERRAIN_MAX_HEIGHT

	hasChangedSize: (width, height, smoothness, zScale) =>
		width != parseInt(@world.get('terrain').width) or height != parseInt(@world.get('terrain').height) or smoothness != parseFloat(@world.get('terrain').smoothness) or zScale != parseInt(@world.get('terrain').zScale)

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
	
	
	createStaticObjects: ->
		if @world.attributes.staticGeometries
			mesh = null
			for instance in @world.attributes.objects
				unless @editor.engine.scenegraph.hasStaticObject instance.geoId 
					for staticMesh in @world.attributes.staticGeometries
						if staticMesh.userData.id is instance.geoId
							mesh = staticMesh
							mesh.name = instance.name
							break
				else
					mesh = @editor.engine.scenegraph.staticObjects[instance.geoId][0]
					mesh = materialHelper.createMesh mesh.geometry, mesh.material.materials, instance.name, id:mesh.userData.dbId
				#add position to mesh ... 
				if mesh
					mesh.position = new THREE.Vector3 instance.position.x,instance.position.y,instance.position.z
					mesh.rotation = new THREE.Vector3 instance.rotation.x,instance.rotation.y,instance.rotation.z
					mesh.scale = new THREE.Vector3 instance.scale.x,instance.scale.y,instance.scale.z
					mesh.userData.meshId = instance.id
					@editor.engine.scenegraph.addStaticObject mesh, mesh.userData.dbId
					id = @editor.engine.scenegraph.getNextId()
					environmentsStatic = db.get 'environmentsStatic'
					environmentsStatic.add @createModelFromMesh id, mesh, mesh.name
		@editor.engine.render()
		null

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

	handleMaterials: =>
		@world.handleMaterials @editor.engine.scenegraph.getMap()
	
	removeStaticObject: (obj) =>
		log.info "Environment \"#{obj.get('name')}\" removed"
		col = db.get 'environmentsStatic'
		col.remove obj
		@editor.engine.scenegraph.removeStaticObject obj.attributes
		EditorEventbus.dispatch 'render'
		
return Scene