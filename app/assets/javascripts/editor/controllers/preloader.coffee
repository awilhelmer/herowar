BaseController = require 'controllers/baseController'
Variables = require 'variables'
Eventbus = require 'eventbus'
db = require 'database'

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
		@state = 1
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
		console.log "Load [type=#{type}, name=#{name}, url=#{url}]"
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
		if @state is 1
			@ctx.font = '24px Arial'
			@ctx.fillStyle = "rgba(200, 200, 200, #{@alpha})"
			@ctx.fillText "Loading #{Math.round(@percentage)}%", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
			@ctx.restore()
			if @progress.finish
				@state = 2
				@loadMap()
		else 
			@ctx.font = '24px Arial'
			@ctx.fillStyle = "rgba(200, 200, 200, #{@alpha})"
			text = if @options.map then "Creating map #{@options.map}" else 'Creating default map'
			@ctx.fillText text, Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
			@ctx.restore()
			if @state is 3
				@alpha -= 0.05
				@finish() if @alpha <= 0 and !@preloadComplete

	loadMap: ->
		jqxhr = $.ajax
			url					: if @options.map then "/api/editor/map/#{@options.map}" else '/api/editor/map/default'
			type				: 'GET'
			dataType		: 'json'
			success			: @onSuccess

	onSuccess: (data) =>
		world = db.get 'world'
		world.loadMaterials data
		world.set @parseWorldData data
		console.log world
		@state = 3
	
	parseWorldData: (data) ->
		if data.terrain.geometry.id
			console.log "Parse geometry id #{data.terrain.geometry.id} with json loader"
			loader = new THREE.JSONLoader()
			result = loader.parse data.terrain.geometry
			materials = data.terrain.geometry.materials
			data.terrain.geometry = result.geometry
			#set in userData our DB-materials ... 
			data.terrain.geometry.userData = {}
			data.terrain.geometry.userData.materials = materials
			data.terrain.geometry
		data

	finish: ->
		console.log 'Preloading complete'
		@preloadComplete = true
		@$container.find('canvas').remove()
		db.data @data
		Backbone.history.loadUrl 'editor2'

return Preloader