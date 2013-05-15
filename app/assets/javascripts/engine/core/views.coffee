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
		@rendering = false
		console.log @viewports

	initViewports: ->
		for view in @viewports.models
			_.defaults view.attributes, @defaultProperties.viewport
			_.defaults view.attributes.camera, @defaultProperties.camera
			$domElement = $ view.get('domId')
			view.set
				'cameraScene'		: @createCamera view
				'cameraSkybox' 	: new THREE.PerspectiveCamera 50, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 1000
				'domElement'		: $domElement.get 0
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
	
	render: (renderer, rendererType, scene, skyboxScene) ->
		if (@rendering == false) 
			@rendering = true
			for view in @viewports.models
					@cameraRender renderer, rendererType, scene, skyboxScene, view
			@rendering = false
		return

	cameraRender : (renderer, rendererType, scene, skyboxScene, view) ->
		size = view.get 'size'
		left = Math.floor Variables.SCREEN_WIDTH * size.left
		top = Math.floor Variables.SCREEN_HEIGHT * size.top
		width = Math.floor Variables.SCREEN_WIDTH * size.width
		height = Math.floor Variables.SCREEN_HEIGHT * size.height 
		if rendererType is Variables.RENDERER_TYPE_WEBGL
			renderer.setViewport left, top, width, height
			renderer.setScissor left, top, width, height 
			renderer.enableScissorTest  true 
		aspect =  width / height
		cameraScene = view.get 'cameraScene'
		cameraSkybox = view.get 'cameraSkybox'
		if cameraScene.aspect isnt aspect
			cameraScene.aspect = aspect
			cameraScene.updateProjectionMatrix()
			cameraSkybox.aspect = cameraScene.aspect
			cameraSkybox.updateProjectionMatrix()
		cameraSkybox.rotation.copy cameraScene.rotation
		renderer.render skyboxScene, cameraSkybox
		renderer.render scene, cameraScene
		return

	resizeViews: ->
		for view in @viewports.models
			cameraScene = view.get 'cameraScene'
			if cameraScene instanceof THREE.OrthographicCamera
				cameraScene.left = Variables.SCREEN_WIDTH / - 2
				cameraScene.right = Variables.SCREEN_WIDTH / 2
				cameraScene.top = Variables.SCREEN_HEIGHT / 2
				cameraScene.bottom = Variables.SCREEN_HEIGHT / - 2
		return
			
return Views