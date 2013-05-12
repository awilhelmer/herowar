SelectorTerrain = require 'tools/selectorTerrain'
materialHelper = require 'helper/materialHelper'
db = require 'database'

class AddObject extends SelectorTerrain

	constructor: (@app, @intersectHelper) ->
		@input = db.get 'input'
		@tool = db.get 'ui/tool'
		@tool.set
			'currentObjectId' 	: -1
			'currentObjectName'	: null
		super @app, @intersectHelper

	onLeaveTool: ->
		if @tool.get('currentObject')
			@app.engine.scenegraph.scene.remove @tool.get('currentObject')
			for child in @tool.get('currentObject').children
				child.geometry.dispose() # TODO: is this enough clean up ?!?
			@tool.unset 'currentObject'

	onIntersect: ->
		@tool.get('currentObject').visible = true if @tool.get('currentObject') and !@tool.get('currentObject').visible

	onNonIntersect: ->
		if @tool.get('currentObject')?.visible
			@tool.get('currentObject').visible = false

	update: (position, intersect) ->
		if @tool.get('currentObject')
			@tool.get('currentObject').position = position

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentObject')?.visible and !@input.get('mouse_moved') if event.which is 1

	onLoadGeometry: (geometry, materials, json) =>
		json = id: @tool.get('currentObjectId') unless json
		mesh = materialHelper.createMesh geometry, materials, @tool.get('currentObjectName'), json
		obj = new THREE.Object3D()
		obj.name = mesh.name
		obj.add mesh
		@tool.set 'currentObject', obj
		@addMesh()

	addMesh: ->
	
	placeMesh: ->

return AddObject