EditorEventbus = require 'editorEventbus'
Environment = require 'models/environment'
JSONLoader = require 'util/threeloader'
Constants = require 'constants'
log = require 'util/logger'
AddObject = require 'tools/addObject'
engine = require 'engine'
db = require 'database'

class AddEnvironment extends AddObject

	constructor: (@intersectHelper) ->
		@loader = new JSONLoader()
		super @intersectHelper
	
	bindEvents: ->
		EditorEventbus.listSelectItem.add @onSelectItem
		EditorEventbus.changeStaticObject.add @changeStaticObject

	onLeaveTool: ->
		super()
		engine.render()

	onNonIntersect: ->
		super()
		engine.render()

	update: (position, intersect) ->
		super position, intersect
		engine.render()

	onSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-geometries-list' and @tool.get('currentObjectId') isnt value
			log.debug 'Set Tool Build'
			@tool.set
				'active'					: Constants.TOOL_BUILD
				'currentObjectId' 	: value
				'currentObjectName'	: name
			unless engine.scenegraph.hasStaticObject @tool.get 'currentObjectId'
				log.debug "Loading Geometry from Server ... "
				now = new Date()
				@loader.load "/api/game/geometry/env/#{@tool.get('currentObjectId')}", @onLoadGeometry, 'assets/images/game/textures'
				log.debug "Loading Geometry from Server completed, time  #{new Date().getTime() - now.getTime()} ms"
			else
				mesh = engine.scenegraph.staticObjects[@tool.get('currentObjectId')][0]
				@onLoadGeometry mesh.geometry, mesh.material.materials

	changeStaticObject: (backboneModel) =>
		mesh = engine.scenegraph.getStaticObject backboneModel.get('dbId'), backboneModel.get('listIndex')
		attributes = _.pick _.clone(backboneModel.attributes), 'position', 'scale', 'rotation'
		_.extend mesh, attributes
		engine.render()

	addMesh: ->
		mesh = @tool.get('currentObject')
		engine.scenegraph.addStaticObject mesh, @tool.get('currentObjectId')
		engine.render()

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
		id = engine.scenegraph.getNextId()
		environmentsStatic = db.get 'environmentsStatic'
		envModel = @createModelFromMesh id, @tool.get('currentObject')
		environmentsStatic.add envModel
		log.info "Environment \"#{envModel.get('name')}\" added"
		@onLoadGeometry @tool.get('currentObject').children[0].geometry, @tool.get('currentObject').children[0].material.materials

return AddEnvironment