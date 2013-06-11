Variables = require 'variables'
Scene = require 'models/scene'
events = require 'events'
db = require 'database'

_data = db.get 'scene'

sceneGraph =

	scene: (name) ->
		name = 'main' unless name
		scenes = _data.get 'scenes'
		scenes[name] = @_createScene name unless _.has scenes, name
		return scenes[name]
	
	update: (delta, now) ->
		for id, obj of _data.get 'dynamicObjects'
			obj.update delta, now
		return

	clear: ->
		dynamicObjects = _data.get 'dynamicObjects'
		for id, obj of dynamicObjects
			@removeDynObject id
		_data.trigger 'change:dynamicObjects'
		_data.trigger 'change'
		return

	getDynObjects: ->
		return _data.get 'dynamicObjects'

	getDynObject: (id) ->
		dynamicObjects = _data.get 'dynamicObjects'
		return dynamicObjects[id]

	addDynObject: (object, id) ->
		dynamicObjects = _data.get 'dynamicObjects'
		unless _.has dynamicObjects, id
			dynamicObjects[id] = object
			@addToScenes object
			_data.trigger 'change:dynamicObjects'
			_data.trigger 'change'

	removeDynObject: (id) ->
		dynamicObjects = _data.get 'dynamicObjects'
		if _.has dynamicObjects, id
			@removeFromScenes dynamicObjects[id]
			delete dynamicObjects[id]
			_data.trigger 'change:dynamicObjects'
			_data.trigger 'change'

	getStaticObject: (id, index) ->
		staticObjects = _data.get 'staticObjects'
		return staticObjects[id][index]
	
	hasStaticObject: (dbId) ->
		staticObjects = _data.get 'staticObjects'
		if _.has(staticObjects, dbId) and staticObjects[dbId]?.length > 0
			return true
		false

	addStaticObject: (obj, id) ->
		staticObjects = _data.get 'staticObjects'
		unless _.has staticObjects, id
			staticObjects[id] = [] 
		obj.userData.listIndex = staticObjects[id].length
		obj.userData.dbId = id
		staticObjects[id].push obj
		@addToScenes obj
		_data.trigger 'change:staticObjects'
		_data.trigger 'change'
	
	removeStaticObject: (obj) ->
		staticObjects = _data.get 'staticObjects'
		if _.has staticObjects, obj.dbId
			threeModel = staticObjects[obj.dbId][obj.listIndex]
			arrIndex = threeModel.userData.listIndex
			@removeFromScenes threeModel
			if staticObjects[obj.dbId][arrIndex]
				staticObjects[obj.dbId].slice arrIndex, 1
		_data.trigger 'change:staticObjects'
		_data.trigger 'change'
		null
	
	getMap: ->
		return @map

	setMap: (mesh) ->
		@removeFromScenes @map
		Terrain = require 'models/scene/terrain'
		@map = new Terrain 
			id       : @getNextId() 
			name     :'Terrain'
			meshBody : mesh
		@addToScenes @map
		return @map

	getNextId: ->
		nextId = _data.get 'currentId'
		_data.set 'currentId', nextId + 1
		return nextId

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

	_createScene: (name) ->
		scene = new THREE.Scene()
		scene.name = "scene-#{name}"
		scene.add new THREE.AmbientLight 0x101010
		directionalLight = new THREE.DirectionalLight 0xffffff 
		directionalLight.position.set(0, 1, 1).normalize()
		scene.add directionalLight
		return scene

	addToScenes: (object) ->
		return unless object
		@scene(scene).add obj for scene, obj of object.root
		return

	removeFromScenes: (object) ->
		return unless object
		@scene(scene).remove obj for scene, obj of object.root when obj.parent
		return

return sceneGraph
