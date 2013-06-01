class BaseComposer
	
	constructor: (@view, @renderTarget) ->
		$domElement = $ @view.get 'domElement'
		width = $domElement.width()
		height = $domElement.height()
		@composer = @_createComposer width, height
		@initialize @composer, width, height
		@composer.passes[@composer.passes.length-1].renderToScreen = true

	initialize: (composer, width, height) ->

	render: (delta) ->
		@composer.render delta
		return
		
	setSize: (width, height) ->
		@composer.setSize width, height
		return

	setCamera: (camera) ->
		camera = @view.get 'cameraScene' unless camera
		pass.camera = camera for pass in @composer.passes when pass instanceof THREE.RenderPass
		return

	reset: ->
		@composer.reset()
		return

	_createComposer: (width, height) ->
		@renderTarget = @_createRenderTarget width, height unless @renderTarget
		return new THREE.EffectComposer @view.get('renderer'), @renderTarget
	
	_createRenderTarget: (width, height) ->
		return new THREE.WebGLRenderTarget width, height, @_createRenderParams()
	
	_createRenderParams: ->
		return minFilter: THREE.LinearFilter, magFilter: THREE.LinearFilter, format: THREE.RGBFormat, stencilBuffer: false

return BaseComposer