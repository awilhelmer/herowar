RendererCanvasController = require 'controllers/rendererCanvas'
materialHelper = require 'helper/materialHelper'
GeometryUtils = require 'util/geometryUtils'
textureUtils = require 'util/textureUtils'
JSONLoader = require 'util/threeloader'
Variables = require 'variables'
Eventbus = require 'eventbus'
log = require 'util/logger'
db = require 'database'

class Preloader extends RendererCanvasController

	id: 'preloader'
	
	redirectTo: ''

	views:
		'views/preloader'	: ''

	initialize: (options) ->
		log.info 'Initialize preloader...'
		@initVariables()			
		super options
		if @options.data
			@load @options.data
		else
			# TODO: get a proper solution here for the editor...
			@load
				textures:
					'stone-natural-001'	: 'assets/images/game/textures/stone/natural-001.jpg'
					'stone-rough-001'		: 'assets/images/game/textures/stone/rough-001.jpg'
					'ground-rock'		: 'assets/images/game/textures/ground/rock.jpg'
					'ground-grass'		: 'assets/images/game/textures/ground/grass.jpg'
					'texture_ground_grass'		: 'assets/images/game/textures/ground/texture_ground_grass.jpg'
					'texture_ground_bare'		: 'assets/images/game/textures/ground/texture_ground_bare.jpg'
					'texture_ground_snow'		: 'assets/images/game/textures/ground/texture_ground_snow.jpg'
				texturesCube:
					'default' 					: 'assets/images/game/skybox/default/%1.jpg'

	initVariables: ->
		@preloadComplete = false
		@alpha = 1.0
		@state = 1
		@percentage = 0
		@data = {}
		@states = {}
		@jsonLoader = new JSONLoader()
		@types = [ 'textures', 'texturesCube', 'geometries', 'images' ]
		for type in @types
			@data[type] = {}
			@states[type] = {}
		@progress =
			total: 0
			remaining: 0
			loaded: 0
			finish: false
			started: true

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
			log.error "Unkown loader type #{type}."
			return
		if state is true
			@progress.remaining--
			@progress.loaded++
			@percentage = @progress.loaded / @progress.total  * 100
		@states[type][name] = state
		@afterUpdateState()
		@progress.finish = true if @progress.loaded is @progress.total

	# Override to do someone after state update
	afterUpdateState: ->

	loadItem: (type, name, url) ->
		log.info "Load [type=#{type}, name=#{name}, url=#{url}]"
		@updateState type, name, false
		switch type
			when 'images'
				img = new Image
				img.onload = () =>
					@data['textures'][name] = textureUtils.createFromImage
						image: @data['images'][name]
						alpha: true
					@updateState type, name, true
				img.src = url
				@data[type][name] = img
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
			when 'geometries'
				@jsonLoader.load url, 
					(geometry, materials, json) =>
						geometry.name = name
						geometry.computeBoundingBox()
						geometry.computeBoundingSphere()
						geometry.computeMorphNormals()
						geometry.computeFaceNormals()
						geometry.computeVertexNormals()
						THREE.GeometryUtils.center geometry
						db.geometry name, 'main', geometry
						db.geometry name, 'glow', GeometryUtils.clone geometry
						@data[type][name] = [geometry, materials, json]
						@updateState type, name, true
					, 'assets/images/game/textures'
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
			if @progress.finish and @progress.started
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
		oldGeo = data.terrain.geometry
		data.terrain.geometry.materials = []
		world.set @parseWorldData data
		newGeo = world.attributes.terrain.geometry
		newGeo.userData = {}
		newGeo.userData.matIdMapper = oldGeo.matIdMapper
		newGeo.userData.id = oldGeo.id
		newGeo.userData.type = oldGeo.type
		newGeo.userData.metadata = oldGeo.metadata
		newGeo.userData.version = oldGeo.version
		newGeo.userData.cdate = oldGeo.cdate
		world.loadMaterials data.materials
		console.log world
		@state = 3
	
	parseWorldData: (data) ->
		if data.terrain.geometry.id
			log.info "Parse geometry id #{data.terrain.geometry.id} with json loader"
			loader = new THREE.JSONLoader()
			result = loader.parse data.terrain.geometry
			data.terrain.geometry = result.geometry
			if data.staticGeometries
				staticMeshes = []
				for object in data.staticGeometries
					result = loader.parse object
					mesh = materialHelper.createMesh result.geometry, result.materials, object.name, object
					mesh.userData.id = object.id
					mesh.userData.matIdMapper = object.matIdMapper
					staticMeshes.push mesh
				data.staticGeometries = staticMeshes
		data

	finish: ->
		log.info 'Preloading complete'
		@preloadComplete = true
		@$container.find('canvas').remove()
		db.data @data
		Backbone.history.loadUrl @redirectTo

return Preloader