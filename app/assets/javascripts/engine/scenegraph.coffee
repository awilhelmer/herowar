

class SceneGraph
	constructor: (engine) ->
		@engine = engine

	init: ->
		@scene = new THREE.Scene()
		light = new THREE.DirectionalLight 0xffffff 
		light.position.set 0, 0, 1
		@scene.add light
		@objects = {}
		@addDummyObject()
		
	update: ->
		#for object in objects
		
	addObject: (object, id) ->
		if !@objects.hasOwnProperty id
			@objects[id] = object
			@scene.add object
			
	removeObject: (id) ->
		if !@objects.hasOwnProperty id
			@scene.remove @objects[id]
			delete @objects[id]
			
			
	addDummyObject: ->
			context =  @engine.canvas.getContext '2d'
			gradient = context.createRadialGradient  @engine.canvas.width / 2,  @engine.canvas.height / 2, 0,  @engine.canvas.width / 2,  @engine.canvas.height / 2,  @engine.canvas.width / 2 
			gradient.addColorStop 0.1, 'rgba(0,0,0,0.15)' 
			gradient.addColorStop 1, 'rgba(0,0,0,0)' 

			context.fillStyle = gradient;
			context.fillRect 0, 0, @engine.canvas.width, @engine.canvas.height

			shadowTexture = new THREE.Texture @engine.canvas
			shadowTexture.needsUpdate = true

			shadowMaterial = new THREE.MeshBasicMaterial { map: shadowTexture, transparent: true } 
			shadowGeo = new THREE.PlaneGeometry 300, 300, 1, 1 
			mesh = new THREE.Mesh shadowGeo, shadowMaterial 
			mesh.position.y = - 250
			mesh.rotation.x = - Math. PI / 2
			@addObject mesh, 1
			
			 
return SceneGraph