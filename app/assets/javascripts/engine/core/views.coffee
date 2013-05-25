scenegraph = require 'scenegraph'
Variables = require 'variables'
Eventbus = require 'eventbus'
events = require 'events'
engine = require 'engine'
db = require 'database'

class Views

	defaultProperties:
		viewport: 
			domId: 'body'
			size: 
				left: 0, top: 0, width: 1, height: 1
			background: 
				r: 0, g: 0, b: 0, a: 1
			rendererType: Variables.RENDERER_TYPE_WEBGL
		camera:
			type: Variables.CAMERA_TYPE_ORTHOGRAPHIC
			position: [ 0, 350, 0 ]
			rotation: [ THREE.Math.degToRad(-90), 0, 0 ]
			zoom: 1.0
			fov: 75
			near: 1
			far: 10000

	constructor: ->
		@viewports = db.get 'ui/viewports'
		@initViewports()
		@initListener()
		@rendering = false
		console.log @viewports

	initListener:  ->
		Eventbus.cameraChanged.add @onCameraChanged
		window.addEventListener 'resize', => @onWindowResize true 
		events.on 'scene:terrain:build', @changeTerrain, @
		return

	initViewports: ->
		for view in @viewports.models
			_.defaults view.attributes, @defaultProperties.viewport
			_.defaults view.attributes.camera, @defaultProperties.camera
			view.createCameraScene()
			view.createCameraSkybox()
			view.createRenderer()
			view.updateSize()
			console.log 'View', view, 'initialized'
		return
	
	render: (scene, skyboxScene) ->
		unless @rendering 
			@rendering = true
			for view in @viewports.models
					@cameraRender view, scene, skyboxScene
			@rendering = false
		return

	cameraRender : (view, scene, skyboxScene) ->
		renderer = view.get 'renderer'
		cameraScene = view.get 'cameraScene'
		cameraSkybox = view.get 'cameraSkybox'
		cameraSkybox.rotation.copy cameraScene.rotation
		renderer.render skyboxScene, cameraSkybox
		renderer.render scene, cameraScene
		return

	onWindowResize: (withReRender) =>
		# TODO: need refactoring
		$viewport = $ '#viewport'
		Variables.SCREEN_WIDTH = $viewport.width()
		Variables.SCREEN_HEIGHT = $viewport.height()
		view.updateSize() for view in @viewports.models
		engine.render() if withReRender
		return

	onCameraChanged: (view) =>
		@cameraRender view, scenegraph.scene, scenegraph.skyboxScene
		return

	changeTerrain: (terrain) ->
		boundingBox = terrain.children[0].geometry.boundingBox
		for view in @viewports.models
			cameraScene = view.get 'cameraScene'
			if cameraScene instanceof THREE.OrthographicCamera
				camera = view.get 'camera'
				camera.size = 
					left: boundingBox.min.x 
					top: boundingBox.min.y
					width: boundingBox.max.x - boundingBox.min.x
					height: boundingBox.max.y - boundingBox.min.y
				camera.offset = 
					left: 0 
					top: 0
				view.updateSize()
		return
			
return Views