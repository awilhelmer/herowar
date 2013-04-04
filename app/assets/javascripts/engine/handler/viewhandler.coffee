Variables = require 'variables'
Eventbus = require 'eventbus'
###
	@author Alexander Wilhelmer
###

class ViewHandler

	constructor: (@engine, @views) ->
		@initCameras()
		@rendering = false

	initCameras: ->
		for view in @views
			view.camera = @createCamera(view)
			view.camera.position.x = view.eye[ 0 ]
			view.camera.position.y = view.eye[ 1 ]
			view.camera.position.z = view.eye[ 2 ]
			view.camera.up.x = view.up[ 0 ]
			view.camera.up.y = view.up[ 1 ]
			view.camera.up.z = view.up[ 2 ]
			view.isUpdate = _.isFunction(view.updateCamera)
		null
		
	createCamera: (view) ->
		switch view.type
			when Variables.CAMERA_TYPE_RTS 	
				camera = new THREE.PerspectiveCamera view.fov, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 10000
			when Variables.CAMERA_TYPE_FREE
				camera = new THREE.PerspectiveCamera view.fov, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 10000
				#TODO we have only one control - for more we just need a control handler or an array
				if @controls == undefined
					@controls = new THREE.TrackballControls camera, @engine.main.get(0) 
					@controls.rotateSpeed = 1.0
					@controls.zoomSpeed = 1.2
					@controls.panSpeed = 0.8
					@controls.noZoom = false
					@controls.noPan = false
					@controls.staticMoving = true
					@controls.dynamicDampingFactor = 0.3 
					@controls.enabled = true
					@controls.addEventListener( 'change', =>
						console.log 'Controls changed ...'
						Eventbus.cameraChanged.dispatch view
						null
					) 
			else 
				throw 'No camera type setted!'
		camera
	
	render: (renderer, scene) ->
		if (@rendering == false) 
			@rendering = true
			@controls.enable = false if @controls
			for view in @views
					@cameraRender(renderer, scene, view)	
			@rendering = false
			@controls.enable = true if @controls
		null

	cameraRender : (renderer, scene, view) ->
		@updateCamera view
		if view.isUpdate
			view.updateCamera view.camera, scene
		left = Math.floor Variables.SCREEN_WIDTH * view.left 
		bottom = Math.floor Variables.SCREEN_HEIGHT * view.bottom
		width = Math.floor Variables.SCREEN_WIDTH * view.width 
		height = Math.floor Variables.SCREEN_HEIGHT * view.height 
		renderer.setViewport left, bottom, width, height
		renderer.setScissor left, bottom, width, height 
		renderer.enableScissorTest  true 
		renderer.setClearColor view.background, view.background.a 
		view.camera.aspect = width / height
		view.camera.updateProjectionMatrix()		 
		renderer.render(scene, view.camera)
		null
		
	updateCamera: (view)  ->
		switch view.type
			when Variables.CAMERA_TYPE_RTS 	
				null
			when Variables.CAMERA_TYPE_FREE
				null
			else
				console.log "No camera logic for #{ view.type } setted"
	
return ViewHandler