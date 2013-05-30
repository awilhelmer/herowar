HorizontalBlurShader = require 'shaders/horizontalBlurShader'
VerticalBlurShader = require 'shaders/verticalBlurShader'
FinalShader = require 'shaders/finalShader'
FXAAShader = require 'shaders/fxaaShader'
EngineRenderer = require 'enginerenderer'
scenegraph = require 'scenegraph'
Variables = require 'variables'

class Viewport extends Backbone.Model

	stats: null

	render: (delta) ->
		renderer = @get 'renderer'
		cameraScene = @get 'cameraScene'
		cameraSkybox = @get 'cameraSkybox'
		renderer.clear()
		composer.render delta for composer in @get 'composers'
		#if scenegraph.skyboxScene and cameraSkybox
		#	cameraSkybox.rotation.copy cameraScene.rotation
		#	renderer.render scenegraph.skyboxScene, cameraSkybox
		#renderer.render scenegraph.scene(), cameraScene
		#renderer.render scenegraph.scene('glow'), cameraScene
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

	createEffects: ->
		$domElement = $ @get 'domElement'
		renderTargetParameters = 
			minFilter: THREE.LinearFilter
			magFilter: THREE.LinearFilter
			format: THREE.RGBFormat
			stencilBuffer: false
		renderTargetGlow = new THREE.WebGLRenderTarget $domElement.width(), $domElement.height(), renderTargetParameters 
		effectFXAA = new THREE.ShaderPass THREE.FXAAShader
		effectFXAA.uniforms[ 'resolution' ].value.set 1 / $domElement.width(), 1 / $domElement.height()		
		hblur = new THREE.ShaderPass new HorizontalBlurShader()
		vblur = new THREE.ShaderPass new VerticalBlurShader()
		bluriness = 3
		hblur.uniforms[ 'h' ].value = bluriness / $domElement.width()
		vblur.uniforms[ 'v' ].value = bluriness / $domElement.height()
		renderModelGlow = new THREE.RenderPass scenegraph.scene('glow'), @get 'cameraScene'
		glowcomposer = new THREE.EffectComposer @get('renderer') #, renderTargetGlow
		glowcomposer.addPass renderModelGlow
		glowcomposer.addPass hblur
		glowcomposer.addPass vblur
		glowcomposer.addPass hblur
		glowcomposer.addPass vblur
		renderModel = new THREE.RenderPass scenegraph.scene(), @get 'cameraScene'
		finalShader = new FinalShader()
		finalShader.uniforms['tGlow'].texture = glowcomposer.renderTarget2
		finalPass = new THREE.ShaderPass finalShader
		finalPass.renderToScreen = true
		renderTarget = new THREE.WebGLRenderTarget $domElement.width(), $domElement.height(), renderTargetParameters 
		finalcomposer = new THREE.EffectComposer @get('renderer') #, renderTarget
		finalcomposer.addPass renderModel
		finalcomposer.addPass finalPass
		@set 'composers', [ glowcomposer, finalcomposer ]
		#@set 'composers', [ finalcomposer ]

		
	createRenderer: ->
		switch @get 'rendererType'
			when Variables.RENDERER_TYPE_CANVAS
				renderer = new THREE.CanvasRenderer
					antialias: false
			when Variables.RENDERER_TYPE_WEBGL
				renderer = new EngineRenderer 
					antialias: true
				renderer.setClearColorHex 0xFFFFFF, 0.0
				renderer.gammaInput = true
				renderer.gammaOutput = true
				renderer.physicallyBasedShading = true
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
		composer.reset() for composer in @get 'composers'
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