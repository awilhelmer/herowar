class EditorBindings
	
	constructor: (app) ->
		@app = app

	init: ->
		console.log 'Adding editor bindings'
		body = $ 'body'
		body.on 'click', '#addCube', @addCube
		
	addCube: =>
		console.log 'Adding Cube'
		@app.engine.scenegraph.addDummyObject()
	
return EditorBindings