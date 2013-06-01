class BaseComposer
	
	constructor: (@view, @renderTarget) ->
		$domElement = $ @view.get 'domElement'
		width = $domElement.width()
		height = $domElement.height()
		@composer = @_createComposer width, height
		@initialize @composer, width, height

	initialize: (composer, width, height) ->

	render: (delta) ->
		@composer.render delta
		
	setSize: (width, height) ->
		@composer.setSize width, height

	reset: ->
		@composer.reset()

	_createComposer: (width, height) ->
		@renderTarget = new THREE.WebGLRenderTarget width, height, @_createRenderTarget() unless @renderTarget
		return new THREE.EffectComposer @view.get('renderer'), @renderTarget
		
	_createRenderTarget: ->
		return minFilter: THREE.LinearFilter, magFilter: THREE.LinearFilter, format: THREE.RGBFormat

return BaseComposer