Variables = require 'variables'
Eventbus = require 'eventbus'
db = require 'database'

class ViewHandler

	constructor: (@engine) ->
		@viewports = db.get 'ui/viewports'
		@initViewports()
		@rendering = false

	initViewports: ->
		throw 'No Views declared' if @viewports.length is 0
		for view in @viewports.models
			camera = @createView view
			camera.position.x = view.get('eye')[0]
			camera.position.y = view.get('eye')[1]
			camera.position.z = view.get('eye')[2]
			camera.up.x = view.get('up')[0]
			camera.up.y = view.get('up')[1]
			camera.up.z = view.get('up')[2]
			camera.lookAt @engine.scenegraph.scene.position
			view.set
				'camera' 				: camera
				'isUpdate' 			: _.isFunction view.get 'updateCamera'
				'skyboxCamera' 	: new THREE.PerspectiveCamera 50, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 1000
		null
		
	createView: (view) ->
		switch view.get 'type'
			when Variables.CAMERA_TYPE_RTS 	
				camera = new THREE.OrthographicCamera Variables.SCREEN_WIDTH / - 2, Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2, Variables.SCREEN_HEIGHT / -2, 1, 10000
			when Variables.CAMERA_TYPE_FREE
				camera = new THREE.PerspectiveCamera view.get('fov'), Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 10000
				#TODO we have only one control - for more we just need a control handler or an array
				if @controls == undefined
					@controls = new THREE.TrackballControls camera, @engine.main.get(0) 
					@controls.rotateSpeed = 1.0
					@controls.zoomSpeed = 1.2
					@controls.panSpeed = 0.8
					@controls.noZoom = false
					@controls.noPan = false
					@controls.noRotate = false
					@controls.staticMoving = true
					@controls.dynamicDampingFactor = 0.3 
					@controls.enabled = true
					@controls.controlLoopActive = false
					#@controls.target.z = 0
					@controls.addEventListener( 'change', =>
						if (@engine.pause)
							Eventbus.cameraChanged.dispatch view
						null
					) 
					Eventbus.controlsChanged.add @onControlsChanged
			else 
				throw 'No camera type setted!'
		camera
	
	render: (renderer, rendererType, scene, skyboxScene) ->
		if (@rendering == false) 
			@rendering = true
			@controls.enable = false if @controls
			for view in @viewports.models
					@cameraRender renderer, rendererType, scene, skyboxScene, view
			@rendering = false
			@controls.enable = true if @controls
		null

	cameraRender : (renderer, rendererType, scene, skyboxScene, view) ->
		@updateCamera view
		if view.get 'isUpdate'
			view.get('updateCamera')(view.get('camera'), scene)
		left = Math.floor Variables.SCREEN_WIDTH * view.get('left') 
		top = Math.floor Variables.SCREEN_HEIGHT * view.get('top')
		width = Math.floor Variables.SCREEN_WIDTH * view.get('width')
		height = Math.floor Variables.SCREEN_HEIGHT * view.get('height') 
		if rendererType is Variables.RENDERER_TYPE_WEBGL
			renderer.setViewport left, top, width, height
			renderer.setScissor left, top, width, height 
			renderer.enableScissorTest  true 
		aspect =  width / height
		if view.get('camera').aspect isnt aspect
			view.get('camera').aspect = aspect
			view.get('camera').updateProjectionMatrix()
			view.get('skyboxCamera').aspect = view.get('camera').aspect
			view.get('skyboxCamera').updateProjectionMatrix()
		view.get('skyboxCamera').rotation.copy view.get('camera').rotation
		renderer.render skyboxScene, view.get('skyboxCamera')
		renderer.render scene, view.get('camera')
		null
		
	updateCamera: (view)  ->
		switch view.get 'type'
			when Variables.CAMERA_TYPE_RTS 	
				null
			when Variables.CAMERA_TYPE_FREE
				if @controls and not @engine.pause
					@controls.update()
			else
				console.log "No camera logic for #{view.get('type')} setted"
		null

	onControlsChanged: (event) =>
		if (@controls)
			@controls.update()
		null

	resizeViews: ->
		for view in @viewports.models
			if view.get('camera') instanceof THREE.OrthographicCamera
				view.get('camera').left = Variables.SCREEN_WIDTH / - 2
				view.get('camera').right = Variables.SCREEN_WIDTH / 2
				view.get('camera').top = Variables.SCREEN_HEIGHT / 2
				view.get('camera').bottom = Variables.SCREEN_HEIGHT / - 2
		return
			
return ViewHandler