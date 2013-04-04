BaseModel = require 'model/basemodel'
###
	SceneGraph handels all object in the actual scene
	
	@author Alexander Wilhelmer
###

class SceneGraph

	constructor: (engine) ->
		@engine = engine

	init: ->
		@scene = new THREE.Scene()
		@dynamicObjects = {}
		@setMap(@createDefaultTerrain())
		@currentId = 1
			
		 
	update: ->
		for key, val of @dynamicObjects
			val.update
	
	addDynObject: (object, id) ->
		if !@dynamicObjects.hasOwnProperty id
			@dynamicObjects[id] = object
			@scene.add object.object3d
	
	removeDynObject: (id) ->
		if !@dynamicObjects.hasOwnProperty id
			@scene.remove @dynamicObjects[id].object3d
			delete @dynamicObjects[id]

	getNextId: ->
		@currentId++
	
	setMap: (map) ->
		if @map != undefined
			scene.remove @map
		@map = map
		@scene.add @map
	
	addDummyObject: ->
		mesh = new THREE.Mesh new THREE.CubeGeometry(200, 200, 200), new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true
		obj = new BaseModel(mesh)
		@addDynObject obj, @getNextId()

	createDefaultMap: ->
		new THREE.Mesh new THREE.PlaneGeometry(2000, 2000, 20, 20), new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true
			

return SceneGraph
