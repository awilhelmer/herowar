Variables = require 'variables'
Eventbus = require 'eventbus'
	
class Preloader

	constructor: (@app) ->

	init: (data) ->
		console.log 'Initialize preloader...'
		@initLoader()			
		@initListener()
		@initRendererContext()
		@load data if data

	initLoader: ->
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

	initRendererContext: ->
		@ctx = @app.engine.renderer.domElement.getContext '2d'
		@ctx.textAlign = 'center'
		
	initListener: ->
		Eventbus.beforeRender.add(@onBeforeRender)
		Eventbus.afterRender.add(@onAfterRender)
	
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

	onBeforeRender: =>
		@ctx.clearRect 0, 0, Variables.SCREEN_WIDTH, Variables.SCREEN_HEIGHT

	onAfterRender: =>
		@ctx.save()
		@ctx.font = '24px Arial'
		@ctx.fillStyle = "rgba(200, 200, 200, #{@alpha})"
		@ctx.fillText "Loading #{Math.round(@percentage)}%", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
		@ctx.restore()
		if @progress.finish
			@alpha -= 0.05
			@finish() if @alpha <= 0

	finish: ->
		console.log 'Preload complete'
		Eventbus.beforeRender.remove @onBeforeRender
		Eventbus.afterRender.remove @onAfterRender
		Eventbus.preloadComplete.dispatch @app

return Preloader