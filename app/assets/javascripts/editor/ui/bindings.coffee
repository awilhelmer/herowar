class Bindings
	
	constructor: (@editor) ->

	init: ->
		console.log 'Initialize editor bindings'
		@bindEvents()

	bindEvents: ->
		body = $ 'body'
		# Menu: File
		body.on 'click', '#fileNewMapEmpty', @fileNewMapEmpty
		body.on 'click', '#fileNewMapGenerated', @fileNewMapGenerated
		body.on 'click', '#fileOpen', @fileOpen
		body.on 'click', '#fileSave', @fileSave
		body.on 'click', '#fileExit', @fileExit
		# Menu: Add
		body.on 'click', '#addTerrain', @addTerrain
		body.on 'click', '#addCube', @addCube

	fileNewMapEmpty: (event) =>
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileNewMapGenerated: (event) =>
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
		@editor.scenegraph().addTerrain()
		@editor.render()

	addCube: (event) =>
		event?.preventDefault()
		console.log 'Adding Cube'
		@editor.scenegraph().addDummyObject()
		@editor.render()
	
return Bindings