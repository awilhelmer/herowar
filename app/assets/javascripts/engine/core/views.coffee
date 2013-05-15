EngineRenderer = require 'enginerenderer'
Variables = require 'variables'
Eventbus = require 'eventbus'
events = require 'events'
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
			position: [ 0, 0, 0 ]
			rotation: [ 0, 0, 0 ]
			zoom: 1.0
			fov: 75
			near: 1
			far: 10000

	constructor: (@engine) ->
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
			$domElement = $ view.get('domId')
			view.set
				'cameraScene'		: @createCamera view
				'cameraSkybox' 	: new THREE.PerspectiveCamera 50, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 1000
				'domElement'		: $domElement.get 0
				'renderer'			: @createRenderer view, $domElement
			@updateViewSize view
		return
		
	createCamera: (view) ->
		camera = view.get 'camera'
		switch view.get 'type'
			when Variables.VIEWPORT_TYPE_RTS 	
				cameraScene = new THREE.OrthographicCamera Variables.SCREEN_WIDTH / - 2, Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2, Variables.SCREEN_HEIGHT / -2, camera.near, camera.far
				cameraScene.rotation.set -Math.PI/2, 0, 0
			when Variables.VIEWPORT_TYPE_EDITOR
				cameraScene = new THREE.PerspectiveCamera camera.fov, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, camera.near, camera.far
				cameraScene.lookAt @engine.scenegraph.scene.position
		cameraScene.position.set camera.position[0], camera.position[1], camera.position[2]
		cameraScene.rotation.set camera.rotation[0], camera.rotation[1], camera.rotation[2]
		cameraScene
	
	createRenderer: (view, $domElement) ->
		switch view.get 'rendererType'
			when Variables.RENDERER_TYPE_CANVAS
				renderer = new THREE.CanvasRenderer
					clearColor: 0xffffff
			when Variables.RENDERER_TYPE_WEBGL
				renderer = new EngineRenderer 
					antialias: true
				renderer.autoClear = false
		$domElement.append renderer.domElement if renderer
		renderer
	
	render: (scene, skyboxScene) ->
		if (@rendering == false) 
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
		@updateViewSize view for view in @viewports.models
		@engine.render() if withReRender
		return

	updateViewSize: (view) ->
		$domElement = $ view.get 'domElement'
		renderer = view.get 'renderer'
		camera = view.get 'camera'
		cameraScene = view.get 'cameraScene'
		cameraSkybox = view.get 'cameraSkybox'
		width = $domElement.width()
		height = $domElement.height()
		renderer.setSize width, height
		aspect =  width / height
		if camera.size
			cameraScene.left = camera.size.left
			cameraScene.right = camera.size.left + camera.size.width
			cameraScene.top = camera.size.top + camera.size.height
			cameraScene.bottom = camera.size.top
		cameraScene.aspect = aspect
		cameraScene.updateProjectionMatrix()
		cameraSkybox.aspect = cameraScene.aspect
		cameraSkybox.updateProjectionMatrix()
		return

	onCameraChanged: (view) =>
		@cameraRender view, @engine.scenegraph.scene, @engine.scenegraph.skyboxScene
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
				@updateViewSize view
		return
			
return Views