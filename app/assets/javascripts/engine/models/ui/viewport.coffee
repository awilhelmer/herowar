EngineRenderer = require 'enginerenderer'
Variables = require 'variables'

class Viewport extends Backbone.Model

	stats: null

	render: (scene, skyboxScene) ->
		renderer = @get 'renderer'
		cameraScene = @get 'cameraScene'
		cameraSkybox = @get 'cameraSkybox'
		if skyboxScene and cameraSkybox
			cameraSkybox.rotation.copy cameraScene.rotation
			renderer.render skyboxScene, cameraSkybox
		renderer.render scene, cameraScene
		@stats.update() if @stats
		return

	createCameraScene: ->
		camera = @get 'camera'
		switch camera.type
			when Variables.CAMERA_TYPE_ORTHOGRAPHIC 	
				cameraScene = new THREE.OrthographicCamera Variables.SCREEN_WIDTH / - 2, Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2, Variables.SCREEN_HEIGHT / -2, camera.near, camera.far
			when Variables.CAMERA_TYPE_PERSPECTIVE
				cameraScene = new THREE.PerspectiveCamera camera.fov, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, camera.near, camera.far
		@set 'cameraScene', cameraScene
		cameraScene

	createCameraSkybox: ->
		cameraSkybox = new THREE.PerspectiveCamera 50, Variables.SCREEN_WIDTH / Variables.SCREEN_HEIGHT, 1, 1000
		@set 'cameraSkybox', cameraSkybox
		cameraSkybox

	createRenderer: ->
		switch @get 'rendererType'
			when Variables.RENDERER_TYPE_CANVAS
				renderer = new THREE.CanvasRenderer
					clearColor: 0xffffff
			when Variables.RENDERER_TYPE_WEBGL
				renderer = new EngineRenderer 
					antialias: true
				# renderer.autoClear = false
		$domElement = $ @get 'domId'
		@set
			'domElement' : $domElement.get 0
			'renderer'   : renderer
		$domElement.append renderer.domElement if renderer
		renderer

	updateSize: ->
		$domElement = $ @get 'domElement'
		renderer = @get 'renderer'
		camera = @get 'camera'
		cameraScene = @get 'cameraScene'
		cameraSkybox = @get 'cameraSkybox'
		width = $domElement.width()
		height = $domElement.height()
		renderer.setSize width, height
		aspect =  width / height
		@updateCamera cameraScene, camera.position, camera.rotation
		@updateOrthographic cameraScene, camera.size, camera.offset, aspect if cameraScene instanceof THREE.OrthographicCamera
		cameraScene.aspect = aspect
		cameraScene.updateProjectionMatrix()
		cameraSkybox.aspect = cameraScene.aspect
		cameraSkybox.updateProjectionMatrix()
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

	updateCamera: (camera, position, rotation) ->
		camera.position.set position[0], position[1], position[2]
		camera.rotation.set rotation[0], rotation[1], rotation[2]
		return

	updateStats: ->
		showStats = @get 'showStats'
		if showStats then @createStats() else @removeStats()
		return
	
	createStats: ->
		unless @stats
			@stats = new Stats()
			@stats.domElement.id = 'fps'
			$('body').append @stats.domElement
		return

	removeStats: ->
		if @stats
			$('body').remove @stats.domElement
			@stats = null
		return

	createHUD: ->
		hud = @get 'hud'
		if hud is Variables.HUD_GAME
			GameHUD = require 'hud/game'
			@hud = new GameHUD @

return Viewport