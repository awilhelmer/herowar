class EditorBindings
	
	constructor: (app) ->
		@app = app

	init: ->
		console.log 'Initialize editor bindings'
		@bindEvents()

	bindEvents: ->
		body = $ 'body'
		# Menu: File
		body.on 'click', '#fileNew', @fileNew
		body.on 'click', '#fileOpen', @fileOpen
		body.on 'click', '#fileSave', @fileSave
		body.on 'click', '#fileExit', @fileExit
		# Menu: Add
		body.on 'click', '#addTerrain', @addTerrain
		body.on 'click', '#addCube', @addCube

	fileNew: (event) =>
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileOpen: (event) =>
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileSave: (event) =>
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileExit: (event) =>
		event?.preventDefault()
		alert 'Not implemented yet...'

	addTerrain: (event) =>
		event?.preventDefault()
		console.log 'Adding Terrain'
		@app.scenegraph().addTerrain()
		@app.render()

	addCube: (event) =>
		event?.preventDefault()
		console.log 'Adding Cube'
		@app.scenegraph().addDummyObject()
		@app.render()
	
return EditorBindings