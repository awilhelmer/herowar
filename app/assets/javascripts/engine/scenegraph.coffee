BaseModel = require 'models/basemodel'
Variables = require 'variables'
events = require 'events'
db = require 'database'

_initialized = false

sceneGraph =

	initialize: ->
		unless _initialized
			_initialized = true
			@scene = new THREE.Scene()
			@skyboxScene = new THREE.Scene()
			@dynamicObjects = {}
			@staticObjects = {}
			@currentId = 1
			@addLights()
	
	addLights: ->
		@scene.add new THREE.AmbientLight 0x666666

	update: (delta) ->
		for id, obj of @dynamicObjects
			obj.update delta
		return

	clear: ->
		for id, obj of @dynamicObjects
			@removeDynObject id
		return

	addDynObject: (object, id) ->
		unless @dynamicObjects.hasOwnProperty id
			@dynamicObjects[id] = object
			@scene.add object.root

	removeDynObject: (id) ->
		if @dynamicObjects.hasOwnProperty id
			@scene.remove @dynamicObjects[id].root
			delete @dynamicObjects[id]

	addStaticObject: (obj, id) ->
		unless _.has(@staticObjects, id)
			@staticObjects[id] = [] 
		obj.userData.listIndex = @staticObjects[id].length
		obj.userData.dbId = id
		@staticObjects[id].push obj
		@scene.add obj
	
	getStaticObject: (id, index) ->
		@staticObjects[id][index]
	
	hasStaticObject: (dbId) ->
		if _.has(@staticObjects, dbId) and @staticObjects[dbId]?.length > 0
			return true
		false
	
	removeStaticObject: (obj) ->
		if _.has(@staticObjects, obj.dbId)
			threeModel = @staticObjects[obj.dbId][obj.listIndex]
			arrIndex = threeModel.userData.listIndex
			@scene.remove threeModel
			if @staticObjects[obj.dbId][arrIndex]
				@staticObjects[obj.dbId].slice arrIndex, 1
		null
		
	getMap: ->
		@map

	setMap: (map) ->
		if @map
			@scene.remove @map
		@map = map
		@scene.add @map

	getNextId: ->
		@currentId++

	addSkybox: (name) ->
		cubeTexture = db.data()['texturesCube'][name]
		shader = THREE.ShaderLib['cube']
		shader.uniforms['tCube'].value = cubeTexture
		skyboxMaterial = new THREE.ShaderMaterial
			fragmentShader  : shader.fragmentShader
			vertexShader    : shader.vertexShader
			uniforms        : shader.uniforms
			depthWrite      : false
			side            : THREE.BackSide
		@skybox = new THREE.Mesh new THREE.CubeGeometry(1000, 1000, 1000), skyboxMaterial
		@skyboxScene.add @skybox

	removeSkybox: ->
		if _.isUndefined @skybox
			@skyboxScene.remove @skybox
			@skybox = undefined

return sceneGraph
