BaseView = require 'views/baseView'
templates = require 'templates'

class ViewerUI extends BaseView

	id: 'viewerUI'
	
	className: 'hidden'
	
	entity: 'viewer'
	
	template: templates.get 'viewerUI.tmpl'
	
	defaultModelState:
		rotationX: 0
		rotationY: 0
		rotationZ: 0
		
	bindEvents: ->
		@listenTo @model, 'fetching:data', @isFetching
		@listenTo @model, 'fetched:data', @isFetched
	
	isFetching: ->
		$span = @$el.find 'span'
		$span.text 'Loading'
		@$el.removeClass 'hidden'
		return
	
	isFetched: (obj) ->
		$span = @$el.find 'span'
		$span.text obj.name
		setTimeout =>
			@$el.addClass 'hidden'
		, 500
		return
	
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

	resetGUI: ->
		@modelState[key] = value for key, value of @defaultModelState
		if @sceneObject
			console.log 'Set rotation from mesh', @sceneObject.meshBody.rotation
			@modelState.rotationX = THREE.Math.radToDeg @sceneObject.meshBody.rotation.x
			@modelState.rotationY = THREE.Math.radToDeg @sceneObject.meshBody.rotation.y
			@modelState.rotationZ = THREE.Math.radToDeg @sceneObject.meshBody.rotation.z
		return

return ViewerUI