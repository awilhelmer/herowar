SelectorTerrain = require 'tools/selectorTerrain'
EditorEventbus = require 'editorEventbus'
Environment = require 'models/environment'
materialHelper = require 'helper/materialHelper'
JSONLoader = require 'util/threeloader'
Constants = require 'constants'
Variables = require 'variables'
log = require 'util/logger'
db = require 'database'

class AddObject extends SelectorTerrain

	constructor: (@editor, @intersectHelper) ->
		@tool = db.get 'ui/tool'
		@tool.set
			'currentMeshId' 	: -1
			'currentMeshName'	: null
		@loader = new JSONLoader()
		super @editor, @intersectHelper

	bindEvents: ->
		EditorEventbus.listSelectItem.add @onSelectItem
		EditorEventbus.changeStaticObject.add @changeStaticObject

	onLeaveTool: ->
		if @tool.get('currentMesh')
			@editor.engine.scenegraph.scene.remove @tool.get('currentMesh')
			@tool.get('currentMesh').geometry.dispose() # TODO: is this enough clean up ?!?
			@tool.unset 'currentMesh'
			@editor.engine.render()

	onIntersect: ->
		@tool.get('currentMesh').visible = true if @tool.get('currentMesh') and !@tool.get('currentMesh').visible

	onNonIntersect: ->
		if @tool.get('currentMesh')?.visible
			@tool.get('currentMesh').visible = false
			@editor.engine.render()

	update: (position, intersect) ->
		@tool.get('currentMesh').position = position
		@editor.engine.render()

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentMesh')?.visible and !Variables.MOUSE_MOVED if event.which is 1
		super event

	onSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-geometries' and @tool.get('currentMeshId') isnt value
			log.debug 'Set Tool Build'
			@tool.set
				'active'					: Constants.TOOL_BUILD
				'currentMeshId' 	: value
				'currentMeshName'	: name
			unless @editor.engine.scenegraph.hasStaticObject @tool.get 'currentMeshId'
				log.debug "Loading Geometry from Server ... "
				now = new Date()
				@loader.load "/api/game/geometry/env/#{@tool.get('currentMeshId')}", @onLoadGeometry, 'assets/images/game/textures'
				log.debug "Loading Geometry from Server completed, time  #{new Date().getTime() - now.getTime()} ms"
			else
				mesh = @editor.engine.scenegraph.staticObjects[@tool.get('currentMeshId')][0]
				@onLoadGeometry mesh.geometry, mesh.material.materials

	onLoadGeometry: (geometry, materials, json) =>
		json = id:@tool.get('currentMeshId') unless json
		@tool.set 'currentMesh', materialHelper.createMesh geometry, materials, @tool.get('currentMeshName'), json
		@addMesh()

	addMesh: ->
		@tool.get('currentMesh').visible = false
		@editor.engine.scenegraph.addStaticObject @tool.get('currentMesh'), @tool.get('currentMeshId')
		@editor.engine.render()

	createModelFromMesh: (id, mesh) ->
		env = new Environment()
		env.set
			id 				: id
			dbId			: mesh.userData.dbId
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
		@onLoadGeometry @tool.get('currentMesh').geometry, @tool.get('currentMesh').material.materials

	changeStaticObject: (backboneModel) =>
		mesh = @editor.engine.scenegraph.getStaticObject backboneModel.get('dbId'), backboneModel.get('listIndex')
		attributes = _.pick _.clone(backboneModel.attributes), 'position', 'scale', 'rotation'
		_.extend mesh, attributes
		@editor.engine.render()

return AddObject