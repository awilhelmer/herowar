BaseController = require 'controllers/baseController'
db = require 'database'
Eventbus = require 'eventbus'
Variables = require 'variables'

class Preloader extends BaseController

	views:
		'views/preloader'	: ''

	initialize: (options) ->
		console.log 'Initialize preloader...'
		super options
		@$container = $ '#preloader'
		@initLoader()			
		@createRenderer()
		@initRendererContext()
		@animate()
		@load
			textures:
				'stone-natural-001'	: 'assets/images/game/textures/stone/natural-001.jpg'
				'stone-rough-001'		: 'assets/images/game/textures/stone/rough-001.jpg'
			texturesCube:
				'default' 					: 'assets/images/game/skybox/default/%1.jpg'

	initLoader: ->
		@preloadComplete = false
		@alpha = 1.0
		@percentage = 0
		@data = {}
		@states = {}
		@types = [ 'textures', 'texturesCube', 'geometries', 'images' ]
		for type in @types
			@data[type] = {}
			@states[type] = {}
		@progress =
			total: 0
			remaining: 0
			loaded: 0
			finish: false

	createRenderer: ->
		@renderer = new THREE.CanvasRenderer
			clearColor: 0x555555
		@$container.append @renderer.domElement
		@renderer.setSize @$container.width(), @$container.height()

	initRendererContext: ->
		@ctx = @renderer.domElement.getContext '2d'
		@ctx.textAlign = 'center'

	load: (data) ->
		for type in @types
			if type of data
				console.log "Load #{type}"
				size = 0
				for key, value of data[type]
					size++
				@progress.total += size
				@progress.remaining += size
				@loadItem type, key, url for key, url of list for type, list of data		

	updateState: (type, name, state) ->
		unless type of @states
			console.log "Unkown loader type #{type}."
			return
		if state is true
			@progress.remaining--
			@progress.loaded++
			@percentage = @progress.loaded / @progress.total  * 100
		@states[type][name] = state
		@progress.finish = true if @progress.loaded is @progress.total

	loadItem: (type, name, url) ->
		console.log "loadItem type=#{type}, name=#{name}, url=#{url}"
		@updateState type, name, false
		switch type
			# TODO: implement geometries and image loading cases
			when 'textures'
				@data[type][name] = THREE.ImageUtils.loadTexture url, undefined, =>
					@updateState type, name, true
			when 'texturesCube'
				urls = [
					url.replace("%1", "px"), url.replace("%1", "nx"),
					url.replace("%1", "py"), url.replace("%1", "ny"),
					url.replace("%1", "pz"), url.replace("%1", "nz")
				]
				@data[type][name] = THREE.ImageUtils.loadTextureCube urls, new THREE.CubeRefractionMapping(), =>
					@updateState type, name, true
			else 
				throw "The loader type '#{type}' is not supported"

	animate: =>
		requestAnimationFrame @animate unless @preloadComplete
		@ctx.clearRect 0, 0, Variables.SCREEN_WIDTH, Variables.SCREEN_HEIGHT
		@ctx.save()
		@ctx.font = '24px Arial'
		@ctx.fillStyle = "rgba(200, 200, 200, #{@alpha})"
		@ctx.fillText "Loading #{Math.round(@percentage)}%", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
		@ctx.restore()
		if @progress.finish
			@alpha -= 0.05
			@finish() if @alpha <= 0 and !@preloadComplete

	finish: ->
		console.log 'Preload complete'
		@preloadComplete = true
		@$container.find('canvas').remove()
		db.data @data
		Backbone.history.loadUrl 'editor2'

return Preloader