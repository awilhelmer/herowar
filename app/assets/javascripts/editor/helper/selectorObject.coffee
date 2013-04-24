EditorEventbus = require 'editorEventbus'
Environment = require 'models/environment'
Variables = require 'variables'
db = require 'database'

class SelectorObject

	constructor: (@editor, @materialHelper, @objectHelper, @intersectHelper) ->
		@currentMeshId = -1
		@currentMesh = null
		@loader = new THREE.JSONLoader()
		@world = db.get 'world'
		@bindEventListeners()

	bindEventListeners: ->
		EditorEventbus.selectWorldUI.add @selectWorld
		EditorEventbus.selectTerrainUI.add @selectTerrain
		EditorEventbus.selectObjectUI.add @selectObject
		EditorEventbus.updateModelMaterial.add @materialUpdate
		EditorEventbus.listSelectItem.add @onSelectItem
		EditorEventbus.changeStaticObject.add @changeStaticObject
	
	onMouseUp: (event) =>
		if event.which is 1
			if @currentMesh
				@placeMesh() if @currentMesh.visible
			else if !Variables.MOUSE_MOVED
						@update()
		else if event.which is 3
			if @currentMesh
				@editor.engine.scenegraph.scene.remove @currentMesh
				@currentMesh.geometry.dispose() # TODO: is this enough clean up ?!?
				@currentMesh = null
				@editor.engine.render()
	
	onMouseMove: =>
		if @currentMesh
			intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ], 1
			if intersectList.length > 0
				@currentMesh.visible = true unless @currentMesh.visible
				@updateMeshPosition @intersectHelper.getIntersectObject intersectList
			else
				if @currentMesh.visible
					@currentMesh.visible = false
					@editor.engine.render()
	
	updateMeshPosition: (intersect) ->
		position = new THREE.Vector3().addVectors intersect.point, intersect.face.normal.clone().applyMatrix4 intersect.object.matrixRotationWorld
		@currentMesh.position = position
		@editor.engine.render()
	
	placeMesh: ->
		id = @editor.engine.scenegraph.getNextId()
		environmentsStatic = db.get 'environmentsStatic'
		environmentsStatic.add @createModelFromMesh id, @currentMesh
		@onLoadGeometry @currentMesh.geometry, @currentMesh.material.materials
	
	update: ->
		@removeSelectionWireframe @editor.engine.scenegraph.getMap(), @selectedType if @selectedObject and @selectedType is 'terrain'
		objects = @intersectHelper.mouseIntersects @editor.engine.scenegraph.scene.children
		if objects.length > 0
			obj = @objectHelper.getBaseObject objects[0].object
			if @objectHelper.isTerrain 
				@selectedType = 'terrain'
				@addSelectionWireframe obj, @selectedType
				EditorEventbus.selectTerrainViewport.dispatch()
			else 
				@selectedType = 'object'
				EditorEventbus.selectObjectViewport.dispatch()
			@selectedObject = obj
		else
			@selectedType = 'world'
			@selectedObject = null
			EditorEventbus.selectWorldViewport.dispatch()
		@editor.engine.render()

	selectWorld: =>
		@removeSelectionWireframe @editor.engine.scenegraph.getMap(), @selectedType if @selectedObject and @selectedType is 'terrain'
		@selectedType = 'world'
		@selectedObject = null
		@editor.engine.render()

	selectTerrain: =>
		if @selectedObject and @selectedType isnt 'terrain'
			@removeSelectionWireframe @selectedObject, @selectedType
		else if @selectedType isnt 'terrain'
			@selectedObject = @editor.engine.scenegraph.getMap()
			@selectedType = 'terrain'
			@addSelectionWireframe @selectedObject, @selectedType
			@editor.engine.render()

	selectObject: =>
		# TODO: implement this ...

	addSelectionWireframe: (obj, type) ->
		if @objectHelper.hasWireframe obj
			@objectHelper.changeWireframeColor obj, 0xFFFF00
		else
			@objectHelper.addWireframe obj, 0xFFFF00

	removeSelectionWireframe: (obj, type) ->
		if @objectHelper.hasWireframe(obj) and type is 'terrain' and @world.get('terrain').wireframe
			@objectHelper.changeWireframeColor obj, 0xFFFFFF
		else
			@objectHelper.removeWireframe obj
	
	materialUpdate: (idMapper) =>
		if idMapper
			mesh = @objectHelper.getModel @editor.engine.scenegraph.getMap()
			matIndex = @materialHelper.updateMaterial mesh, idMapper
			if matIndex > -1 and mesh.material.materials[matIndex].map and mesh.material.materials[matIndex].map.needsUpdate
				@editor.engine.scenegraph.getMap().remove mesh
				@editor.engine.render()
				mesh.geometry.geometryGroups = undefined
				mesh.geometry.geometryGroupsList = undefined
				mesh.__webglInit = false
				mesh.__webglActive = false			
				@editor.engine.scenegraph.getMap().add mesh
			@editor.engine.render()
		null

	onSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-geometries' and @currentMeshId isnt value
			@currentMeshId = value
			@currentMeshName = name
			unless @editor.engine.scenegraph.hasStaticObject(@currentMeshId)
				console.log "Loading Geometry from Server ... "
				now = new Date()
				@loader.load "/api/game/geometry/env/#{@currentMeshId}", @onLoadGeometry, 'assets/images/game/textures'
				console.log "Loading Geometry from Server completed, time  #{new Date().getTime() - now.getTime()} ms"
			else
				mesh = @editor.engine.scenegraph.staticObjects[@currentMeshId][0]
				@onLoadGeometry mesh.geometry, mesh.material.materials
			
	onLoadGeometry: (geometry, materials) =>
		@currentMesh = new THREE.Mesh geometry
		@currentMesh.material = new THREE.MeshFaceMaterial materials
		@currentMesh.name = @currentMeshName
		@currentMesh.dbId = @currentMeshId
		@addMesh()
	
	addMesh: ->
		@currentMesh.visible = false
		@editor.engine.scenegraph.addStaticObject @currentMesh, @currentMeshId
		@editor.engine.render()

	createModelFromMesh: (id, mesh) ->
		env = new Environment()
		env.set
			id 				: id
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

	changeStaticObject: (backboneModel) =>
		console.log 'Change static object'
		mesh = @editor.engine.scenegraph.getStaticObject backboneModel.get 'id'
		console.log mesh
		mesh.position.x = backboneModel.get('position').x
		mesh.position.y = backboneModel.get('position').y
		mesh.position.z = backboneModel.get('position').z
		mesh.scale.x = backboneModel.get('scale').x
		mesh.scale.y = backboneModel.get('scale').y
		mesh.scale.z = backboneModel.get('scale').z
		mesh.rotation.x = backboneModel.get('rotation').x
		mesh.rotation.y = backboneModel.get('rotation').y
		mesh.rotation.z = backboneModel.get('rotation').z

return SelectorObject