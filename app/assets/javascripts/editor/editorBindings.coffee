class EditorBindings
	
	constructor: (app) ->
		@app = app

	init: ->
		console.log 'Adding editor bindings'
		body = $ 'body'
		body.on 'click', '#addTerrain', @addTerrain
		body.on 'click', '#addCube', @addCube

	addTerrain: (event) =>
		event?.preventDefault()
		console.log 'Adding Terrain'
		@app.engine.scenegraph.addTerrain()
		
	addCube: (event) =>
		event?.preventDefault()
		console.log 'Adding Cube'
		@app.engine.scenegraph.addDummyObject()
	
return EditorBindings