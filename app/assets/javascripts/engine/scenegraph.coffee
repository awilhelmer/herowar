BaseModel = require 'model/basemodel'
Variables = require 'variables'

class SceneGraph

	constructor: (@engine) ->
		@init()

	init: ->
		@scene = new THREE.Scene()
		@skyboxScene = new THREE.Scene()
		@dynamicObjects = {}
		#@setMap(@createDefaultMap(2000, 2000))
		@addSkybox 'default'
		@currentId = 1

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
		path = "assets/images/game/skybox/#{name}/"
		format = '.jpg'
		urls = [ path + 'px' + format, path + 'nx' + format,
						 path + 'py' + format, path + 'ny' + format,
						 path + 'pz' + format, path + 'nz' + format ]
		cubeTexture = THREE.ImageUtils.loadTextureCube urls
		shader = THREE.ShaderLib['cube']
		shader.uniforms['tCube'].value = cubeTexture
		skyboxMaterial = new THREE.ShaderMaterial
			fragmentShader  : shader.fragmentShader
			vertexShader    : shader.vertexShader
			uniforms        : shader.uniforms
			depthWrite      : false
			side            : THREE.BackSide
		@skyboxScene.add new THREE.Mesh new THREE.CubeGeometry(100, 100, 100), skyboxMaterial

return SceneGraph
