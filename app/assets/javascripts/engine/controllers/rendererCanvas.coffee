BaseController = require 'controllers/baseController'

class RendererCanvasController extends BaseController
	
	id: ''
	
	initialize: (options) ->
		super options
		@$container = $ "##{@id}"
		@createRenderer()
		@initRendererContext()
		@animate()

	createRenderer: ->
		@renderer = new THREE.CanvasRenderer
			clearColor: 0x555555
		@$container.append @renderer.domElement
		@renderer.setSize @$container.width(), @$container.height()

	initRendererContext: ->
		@ctx = @renderer.domElement.getContext '2d'
		@ctx.textAlign = 'center'
		
	animate: =>		
	
return RendererCanvasController