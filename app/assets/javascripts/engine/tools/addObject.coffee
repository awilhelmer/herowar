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

	onNonIntersect: ->
		@tool.get('currentObject').visible = false if @tool.get('currentObject')?.visible

	update: (position, intersect) ->
		if @tool.get('currentObject')
			@tool.get('currentObject').position = position
			@tool.get('currentObject').visible = true unless @tool.get('currentObject').visible

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentObject')?.visible and !@input.get('mouse_moved') if event.which is 1

	onLoadGeometry: (geometry, materials, json) =>
		unless json
			json = _.extend id: @tool.get('currentObjectId'), json 
		obj = @createThreeObject geometry, materials, @tool.get('currentObjectName'), json
		obj.visible = false
		@tool.set 'currentObject', obj
		@addMesh()
	
	createThreeObject: (geometry, materials, name, json) ->
		mesh = @createMesh geometry, materials, name, json
		obj = new THREE.Object3D()
		obj.name = mesh.name
		obj.add mesh
		return obj
		
	createMesh: (geometry, materials, name, json) ->
		mesh = materialHelper.createMesh geometry, materials, name, json
		if _.isObject json
			mesh.scale.x = json.scale
			mesh.scale.y = json.scale
			mesh.scale.z = json.scale
		mesh.geometry.computeBoundingBox()
		return mesh

	addMesh: ->
	
	placeMesh: ->

return AddObject