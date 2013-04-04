class SceneGraph

	constructor: (engine) ->
		@engine = engine

	init: ->
		@scene = new THREE.Scene()
		@objects = {}
		@currentId = 1
		@addTerrain()
		
	update: ->
		for key, val of @objects
			val.rotation.x += 0.005;
			val.rotation.y += 0.01;

	
	addObject: (object, id) ->
		if !@objects.hasOwnProperty id
			@objects[id] = object
			@scene.add object
	
	removeObject: (id) ->
		if !@objects.hasOwnProperty id
			@scene.remove @objects[id]
			delete @objects[id]

	getNextId: ->
		@currentId++
			
	addDummyObject: ->
		cube = new THREE.Mesh new THREE.CubeGeometry(200, 200, 200), new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true
		@addObject cube, @getNextId()

	addTerrain: ->
		cube = new THREE.Mesh new THREE.PlaneGeometry(2000, 2000, 20, 20), new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true
		@addObject cube, @getNextId()		

return SceneGraph
