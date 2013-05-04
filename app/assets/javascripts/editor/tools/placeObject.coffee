EditorEventbus = require 'editorEventbus'
Environment = require 'models/environment'
materialHelper = require 'helper/materialHelper'
JSONLoader = require 'util/threeloader'
log = require 'util/logger'
db = require 'database'

class PlaceObject

	constructor: (@editor, @intersectHelper) ->
		@tool = db.get 'ui/tool'
		@tool.set
			'currentMeshId' 	: -1
			'currentMeshName'	: null
		@loader = new JSONLoader()
		@bindEventListeners()

	bindEventListeners: ->
		EditorEventbus.listSelectItem.add @onSelectItem
		EditorEventbus.changeStaticObject.add @changeStaticObject

	onMouseUp: (event) ->
		if event.which is 1
			@placeMesh() if @tool.get('currentMesh')?.visible
		else if event.which is 3
			if @tool.get('currentMesh')
				@editor.engine.scenegraph.scene.remove @tool.get('currentMesh')
				@tool.get('currentMesh').geometry.dispose() # TODO: is this enough clean up ?!?
				@tool.unset 'currentMesh'
				@editor.engine.render()

	onMouseMove: (event) ->
		if @tool.get('currentMesh')
			intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ], 1
			if intersectList.length > 0
				@tool.get('currentMesh').visible = true unless @tool.get('currentMesh').visible
				@updateMeshPosition @intersectHelper.getIntersectObject intersectList
			else
				if @tool.get('currentMesh').visible
					@tool.get('currentMesh').visible = false
					@editor.engine.render()

	onSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-geometries' and @tool.get('currentMeshId') isnt value
			@tool.set
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

	updateMeshPosition: (intersect) ->
		position = new THREE.Vector3().addVectors intersect.point, intersect.face.normal.clone().applyMatrix4 intersect.object.matrixRotationWorld
		@tool.get('currentMesh').position = position
		@editor.engine.render()
	
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

return PlaceObject