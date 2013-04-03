

class SceneGraph
	constructor: (engine) ->
		@engine = engine

	init: ->
		@scene = new THREE.Scene()
		@objects = {}
		@addDummyObject()
		
	update: ->
		testMesh = @objects[1] 
		testMesh.rotation.x += 0.005;
		testMesh.rotation.y += 0.01;

	
	addObject: (object, id) ->
		if !@objects.hasOwnProperty id
			@objects[id] = object
			@scene.add object
			
	removeObject: (id) ->
		if !@objects.hasOwnProperty id
			@scene.remove @objects[id]
			delete @objects[id]
			
			
	addDummyObject: ->
			cube = new THREE.Mesh new THREE.CubeGeometry( 200, 200, 200 ), new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true
			@addObject cube,1
			
			 
return SceneGraph
