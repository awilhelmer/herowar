SelectOnTerrain = require 'tools/selectOnTerrain'
materialHelper = require 'helper/materialHelper'
meshesFactory = require 'factory/meshes'
scenegraph = require 'scenegraph'
db = require 'database'

class AddObject extends SelectOnTerrain

	constructor: (@intersectHelper) ->
		@input = db.get 'input'
		@tool = db.get 'ui/tool'
		@tool.set
			'currentObjectId' 	: -1
			'currentObjectName'	: null
		super @intersectHelper

	onLeaveTool: ->
		if @tool.get('currentObject')
			scenegraph.scene().remove @tool.get('currentObject')
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
		obj = @createThreeObject @tool.get('currentObjectId'), @tool.get('currentObjectName')
		obj.visible = false
		@tool.set 'currentObject', obj
		@addMesh()
	
	createThreeObject: (id, name) ->
		obj = new THREE.Object3D()
		obj.name = name
		obj.add @createMesh id, name
		return obj
		
	createMesh: (id, name) ->
		return meshesFactory.create id, name

	addMesh: ->
	
	placeMesh: ->

return AddObject