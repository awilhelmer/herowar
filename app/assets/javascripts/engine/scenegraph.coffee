BaseModel = require 'models/basemodel'
Variables = require 'variables'

class SceneGraph

	constructor: (@engine) ->
		@init()

	init: ->
		@scene = new THREE.Scene()
		@skyboxScene = new THREE.Scene()
		@dynamicObjects = {}
		@staticObjects =  {}
		#@setMap(@createDefaultMap(2000, 2000))
		#@addSkybox 'default'
		@currentId = 1
		@addLights()
	
	addLights: ->
		@scene.add new THREE.AmbientLight 0x666666

	update: ->
		for id, obj of @dynamicObjects
			obj.update()

	clear: ->
		for id, obj of @dynamicObjects
			@removeDynObject id

	addDynObject: (object, id) ->
		unless @dynamicObjects.hasOwnProperty id
			@dynamicObjects[id] = object
			@scene.add object.object3d

	removeDynObject: (id) ->
		unless @dynamicObjects.hasOwnProperty id
			@scene.remove @dynamicObjects[id].object3d
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
	
	addDummyObject: ->
		mesh = new THREE.Mesh new THREE.CubeGeometry(200, 200, 200), new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true
		obj = new BaseModel(mesh)
		@addDynObject obj, @getNextId()

	createDefaultMap: (width, height) ->
		THREE.SceneUtils.createMultiMaterialObject new THREE.PlaneGeometry(width, height, Math.round(width / 100), Math.round(height / 100)), 
		[ new THREE.MeshBasicMaterial(color: 0x006600), new THREE.MeshBasicMaterial(color: 0xFFFFFF, wireframe: true) ]

	addSkybox: (name) ->
		cubeTexture = @engine.getData 'texturesCube', name
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

return SceneGraph
