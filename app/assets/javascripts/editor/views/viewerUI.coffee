materialHelper = require 'helper/materialHelper'
AnimatedModel = require 'models/animatedModel'
enemiesFactory = require 'factory/enemies'
JSONLoader = require 'util/threeloader'
BaseView = require 'views/baseView'
scenegraph = require 'scenegraph'
templates = require 'templates'
db = require 'database'

__previous_id__ = null

class ViewerUI extends BaseView

	id: 'viewerUI'
	
	template: templates.get 'viewerUI.tmpl'
	
	defaultModelState:
		rotationX: 0
		rotationY: 0
		rotationZ: 0
		
	sceneObject: null
	
	initialize: (options) ->
		db.data().geometries = {}
		@initializeGUI()
		@isLoading = false
		@jsonLoader = new JSONLoader()
		@units = db.get 'db/units'
		@units.fetch()
		super options
		
	bindEvents: ->
		@listenTo @units, 'add remove change reset', @updateSelection

	initializeGUI: ->
		@gui = new dat.GUI()
		@modelState = _.clone @defaultModelState
		@guiModelFolder = @gui.addFolder 'Model'
		@guiModelElements = {}
		@gui.add(@modelState, 'rotationX', -360, 360).listen().onChange =>
			if @sceneObject
				console.log 'Set rotation x', THREE.Math.degToRad @modelState.rotationX 
				@sceneObject.meshBody.rotation.x = THREE.Math.degToRad @modelState.rotationX 
		@gui.add(@modelState, 'rotationY', -360, 360).listen().onChange =>
			@sceneObject.meshBody.rotation.y = THREE.Math.degToRad @modelState.rotationY if @sceneObject		
		@gui.add(@modelState, 'rotationZ', -360, 360).listen().onChange =>
			@sceneObject.meshBody.rotation.z = THREE.Math.degToRad @modelState.rotationZ if @sceneObject		

	loadModel: (id, type, name) ->
		return if @isLoading
		if db.data().geometries[name]
			@placeModel id, name, type
			return
		url = null
		if type is 1
			url = "api/game/geometry/tower/#{id}"
		else if type is 2
			url = "api/game/geometry/unit/#{id}"
		return unless url
		@isLoading = true
		@jsonLoader.load url, 
			(geometry, materials, json) =>
				geometry.name = name
				geometry.computeBoundingBox()
				geometry.computeBoundingSphere()
				geometry.computeMorphNormals()
				db.data().geometries[name] = [geometry, materials, json]
				@placeModel id, name, type
				@isLoading = false
			, 'assets/images/game/textures'
		return

	placeModel: (id, name, type) ->
		scenegraph.removeDynObject __previous_id__ if __previous_id__
		if type is 2
			@sceneObject = enemiesFactory.create @units.get(id).attributes
		else
			data = db.data().geometries[name]
			mesh = @createMesh data[0], data[1], name, data[2]
			@sceneObject = new AnimatedModel id, name, mesh
			scenegraph.addDynObject @sceneObject, id
		__previous_id__ = @sceneObject.id
		@resetGUI()
		console.log 'Show model', @sceneObject, 'Set previous id', __previous_id__
		return

	resetGUI: ->
		@modelState[key] = value for key, value of @defaultModelState
		if @sceneObject
			console.log 'Set rotation from mesh', @sceneObject.meshBody.rotation
			@modelState.rotationX = THREE.Math.radToDeg @sceneObject.meshBody.rotation.x
			@modelState.rotationY = THREE.Math.radToDeg @sceneObject.meshBody.rotation.y
			@modelState.rotationZ = THREE.Math.radToDeg @sceneObject.meshBody.rotation.z
		return
	
	updateSelection: ->
		selection = {}
		selection.enemies = []
		for unit, idx in @units.models
			name = unit.get 'name'
			unless _.has @guiModelElements, name
				cb = (id, type, name) =>
					return () => @loadModel id, type, name
				@modelState[name] = cb unit.id, 2, name
				@modelState[name]() unless @isLoading and @sceneObject
				@guiModelElements[name] = @guiModelFolder.add(@modelState, name).name name

	createMesh: (geometry, materials, name, json) ->
		mesh = materialHelper.createMesh geometry, materials, name, json if json.morphTargets.length is 0
		mesh = materialHelper.createAnimMesh geometry, materials, name, json if json.morphTargets.length isnt 0
		if _.isObject json
			mesh.scale.x = json.scale
			mesh.scale.y = json.scale
			mesh.scale.z = json.scale
		mesh.geometry.computeBoundingBox()
		mesh.position.y = - json.scale * mesh.geometry.boundingBox.min.y
		return mesh

return ViewerUI