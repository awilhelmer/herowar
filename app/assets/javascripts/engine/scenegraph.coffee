BaseModel = require 'models/basemodel'
Variables = require 'variables'
events = require 'events'
db = require 'database'

_initialized = false
_scenes = {}

sceneGraph =

	currentId: 1

	dynamicObjects: {}
	
	staticObjects: {}

	scene: new THREE.Scene()
	
	sceneSkybox: new THREE.Scene()
	
	sceneLasers: new THREE.Scene()

	scene: (name) ->
		name = 'main' unless name
		_scenes[name] = @createScene() unless _.has _scenes, name
		return _scenes[name]
	
	createScene: ->
		scene = new THREE.Scene()
		scene.add new THREE.AmbientLight 0x101010
		directionalLight = new THREE.DirectionalLight 0xffffff 
		directionalLight.position.set(0, 1, 1).normalize()
		scene.add directionalLight
		return scene
	
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
			@scene().add object.root

	removeDynObject: (id) ->
		if @dynamicObjects.hasOwnProperty id
			@scene().remove @dynamicObjects[id].root
			delete @dynamicObjects[id]

	addStaticObject: (obj, id) ->
		unless _.has(@staticObjects, id)
			@staticObjects[id] = [] 
		obj.userData.listIndex = @staticObjects[id].length
		obj.userData.dbId = id
		@staticObjects[id].push obj
		@scene().add obj
	
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
			@scene().remove threeModel
			if @staticObjects[obj.dbId][arrIndex]
				@staticObjects[obj.dbId].slice arrIndex, 1
		null
	
	addLaser: (object, id) ->
		unless @dynamicObjects.hasOwnProperty id
			@dynamicObjects[id] = object
			@scene('glow').add object.root	

	removeLaser: (id) ->
		if @dynamicObjects.hasOwnProperty id
			@scene('glow').remove @dynamicObjects[id].root
			delete @dynamicObjects[id]
	
	getMap: ->
		return @map

	setMap: (map) ->
		if @map
			@scene().remove @map
		@map = map
		@scene().add @map
		return @map

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
		@scene('skybox').add @skybox

	removeSkybox: ->
		if _.isUndefined @skybox
			@scene('skybox').remove @skybox
			@skybox = undefined

return sceneGraph
