SelectorTerrain = require 'tools/selectorTerrain'
Environment = require 'models/environment'
materialHelper = require 'helper/materialHelper'
JSONLoader = require 'util/threeloader'
Constants = require 'constants'
Variables = require 'variables'
log = require 'util/logger'
db = require 'database'

class AddObject extends SelectorTerrain

	constructor: (@editor, @intersectHelper) ->
		@input = db.get 'input'
		@tool = db.get 'ui/tool'
		@tool.set
			'currentMeshId' 	: -1
			'currentMeshName'	: null
		@loader = new JSONLoader()
		super @editor, @intersectHelper

	onLeaveTool: ->
		if @tool.get('currentMesh')
			@editor.engine.scenegraph.scene.remove @tool.get('currentMesh')
			for child in @tool.get('currentMesh').children
				child.geometry.dispose() # TODO: is this enough clean up ?!?
			@tool.unset 'currentMesh'
			@editor.engine.render()

	onIntersect: ->
		@tool.get('currentMesh').visible = true if @tool.get('currentMesh') and !@tool.get('currentMesh').visible

	onNonIntersect: ->
		if @tool.get('currentMesh')?.visible
			@tool.get('currentMesh').visible = false
			@editor.engine.render()

	update: (position, intersect) ->
		if @tool.get('currentMesh')
			@tool.get('currentMesh').position = position
			@editor.engine.render()

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentMesh')?.visible and !@input.get('mouse_moved') if event.which is 1

	onLoadGeometry: (geometry, materials, json) =>
		json = id: @tool.get('currentMeshId') unless json
		mesh = materialHelper.createMesh geometry, materials, @tool.get('currentMeshName'), json
		obj = new THREE.Object3D()
		obj.name = mesh.name
		obj.add mesh
		@tool.set 'currentMesh', obj
		@addMesh()

	addMesh: ->
		mesh = @tool.get('currentMesh')
		mesh.visible = false
		@editor.engine.scenegraph.addStaticObject mesh, @tool.get('currentMeshId')
		@editor.engine.render()

	createModelFromMesh: (id, mesh) ->
		env = new Environment()
		env.set
			id 				: id
			dbId			: mesh.userData.dbId
			meshId		:	id * -1
			listIndex	: mesh.userData.listIndex
			name 			: "#{mesh.name}-#{id}"
			position	:
				x				: mesh.position.x
				y				: mesh.position.y
				z				: mesh.position.z
			rotation	:
				x				: mesh.rotation.x
				y				: mesh.rotation.y
				z				: mesh.rotation.z
			scale			:
				x				: mesh.scale.x
				y				: mesh.scale.y
				z				: mesh.scale.z
		env
	
	placeMesh: ->
		id = @editor.engine.scenegraph.getNextId()
		environmentsStatic = db.get 'environmentsStatic'
		envModel = @createModelFromMesh id, @tool.get('currentMesh')
		environmentsStatic.add envModel
		log.info "Environment \"#{envModel.get('name')}\" added"
		@onLoadGeometry @tool.get('currentMesh').children[0].geometry, @tool.get('currentMesh').children[0].material.materials

return AddObject