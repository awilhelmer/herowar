EngineRenderer = require 'enginerenderer'
FinalComposer = require 'composers/final'
Variables = require 'variables'

class Viewport extends Backbone.Model

	stats: null

	render: (delta) ->
		@get('composer').render delta if @has 'composer'
		#if scenegraph.skyboxScene and cameraSkybox
		#	cameraSkybox.rotation.copy cameraScene.rotation
		#	renderer.render scenegraph.skyboxScene, cameraSkybox
		#renderer.render scenegraph.scene(), cameraScene
		#renderer.render scenegraph.scene('glow'), cameraScene
		return

	createCameraScene: ->
		camera = @get 'camera'
		switch camera.type
			when Variables.CAMERA_TYPE_ORTHOGRAPHIC 	
				cameraScene = new THREE.OrthographicCamera Variables.SCREEN_WIDTH / - 2, Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2, Variables.SCREEN_HEIGHT / -2, camera.near, camera.far
			when Variables.CAMERA_TYPE_PERSPECTIVE
				cameraScene = new THREE.PerspectiveCamera camera.fov, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, camera.near, camera.far
		@set 'cameraScene', cameraScene
		@get('composer').setCamera cameraScene if @has 'composer'
		cameraScene

	createCameraSkybox: ->
		cameraSkybox = new THREE.PerspectiveCamera 50, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 1000
		@set 'cameraSkybox', cameraSkybox
		cameraSkybox

	createEffects: ->
		@set 'composer', new FinalComposer @
		
	createRenderer: ->
		switch @get 'rendererType'
			when Variables.RENDERER_TYPE_CANVAS
				renderer = new THREE.CanvasRenderer
					antialias: false
			when Variables.RENDERER_TYPE_WEBGL
				renderer = new EngineRenderer()
				#renderer.gammaInput = true
				#renderer.gammaOutput = true
				#renderer.physicallyBasedShading = true
		$domElement = $ @get 'domId'
		renderer.setSize $domElement.width(), $domElement.height()
		@set
			'domElement' : $domElement.get 0
			'renderer'   : renderer
		$domElement.append renderer.domElement if renderer
		renderer

	resize: ->
		$domElement = $ @get 'domElement'
		width = $domElement.width()
		height = $domElement.height()
		renderer = @get 'renderer'
		renderer.setSize width, height
		aspect =  width / height
		@updateCamera aspect
		if @has 'composer'
			composer = @get 'composer'
			composer.setSize width, height
			composer.reset()
		@hud.resize() if @hud
		return
		
	updateOrthographic: (camera, size, offset, aspect) ->
		return unless size or offset
		aspectSize = size.width / size.height
		aspectReal = Math.max aspect, aspectSize
		if aspectReal > 1
			height = Math.round size.height / aspectReal
			scrollable = size.height - height
			offset.left = 0 if offset.left isnt 0
			offset.top = scrollable if offset.top > scrollable
			camera.left = size.left
			camera.right = size.left + size.width
			camera.top = size.top + size.height - offset.top
			camera.bottom = size.top + (size.height - height) - offset.top
		else
			# TODO: here is something wrong, just make your browser higher than smaller ...
			width = Math.round size.width / aspectReal
			left = size.left + Math.round(width / 2)
			scrollable = size.width - width
			offset.left = scrollable if offset.left > scrollable
			offset.top = 0 if offset.top isnt 0
			camera.left = left + offset.left
			camera.right = left + width + offset.left
			camera.top = size.top + size.height
			camera.bottom = size.top
		#console.log 'New Camera Position -> left=', camera.left, 'right=', camera.right, 'top=', camera.top, 'bottom=', camera.bottom, 'offset', offset
		return

	updateCamera: (aspect) ->
		unless aspect
			renderer = @get 'renderer'
			width = renderer.domElement.offsetWidth
			height = renderer.domElement.offsetHeight
			aspect = width / height
		cameraProperties = @get 'camera'
		cameraScene = @get 'cameraScene'
		cameraScene.position.set cameraProperties.position[0], cameraProperties.position[1], cameraProperties.position[2]
		cameraScene.rotation.set cameraProperties.rotation[0], cameraProperties.rotation[1], cameraProperties.rotation[2]
		@updateOrthographic cameraScene, cameraProperties.size, cameraProperties.offset, aspect if cameraScene instanceof THREE.OrthographicCamera
		cameraScene.aspect = aspect
		cameraScene.updateProjectionMatrix()
		cameraScene.updateMatrixWorld()
		cameraSkybox = @get 'cameraSkybox'
		cameraSkybox.aspect = cameraScene.aspect
		cameraSkybox.updateProjectionMatrix()
		return
	
	createHUD: ->
		hud = @get 'hud'
		if hud is Variables.HUD_GAME
			GameHUD = require 'hud/game'
			@hud = new GameHUD @

return Viewport