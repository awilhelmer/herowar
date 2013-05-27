materialHelper = require 'helper/materialHelper'
AnimatedModel = require 'models/animatedModel'
JSONLoader = require 'util/threeloader'
BaseView = require 'views/baseView'
scenegraph = require 'scenegraph'
templates = require 'templates'
db = require 'database'

class ViewerUI extends BaseView

	id: 'viewerUI'
	
	template: templates.get 'viewerUI.tmpl'
	
	events:
		'change .model' : 'selectModel'
	
	initialize: (options) ->
		@jsonLoader = new JSONLoader()
		@units = db.get 'units'
		@units.fetch()
		@data = 1: [], 2: []
		super options
		
	bindEvents: ->
		@listenTo @units, 'add remove change reset', @render
	
	getTemplateData: ->
		json = {}
		json.enemies = []
		json.enemies.push { id: unit.id, name: unit.get('name'), type: 2 } for unit in @units.models
		json
	
	selectModel: (event) ->
		return unless event
		$currentTarget = $(event.currentTarget).find 'option:selected'
		id = $currentTarget.data 'id'
		type = $currentTarget.data 'type'
		return unless id and type
		console.log "Select model ... #{$currentTarget.val()} / #{id} / #{type}"
		@loadModel id, type

	loadModel: (id, type) ->
		url = null
		if type is 1
			url = "api/game/geometry/tower/#{id}"
		else if type is 2
			url = "api/game/geometry/unit/#{id}"
		return unless url
		@jsonLoader.load url, 
			(geometry, materials, json) =>
				geometry.name = name
				geometry.computeBoundingBox()
				geometry.computeBoundingSphere()
				geometry.computeMorphNormals()
				@data[type][id] = [geometry, materials, json]
				console.log 'Loaded:', geometry, materials, json
				@placeModel id, name, type
			, 'assets/images/game/textures'

	placeModel: (id, name, type) ->
		data = @data[type][id]
		mesh = @createMesh data[0], data[1], name, data[2]
		model = new AnimatedModel 1, name, mesh
		scenegraph.addDynObject model, 1

	createMesh: (geometry, materials, name, json) ->
		mesh = materialHelper.createAnimMesh geometry, materials, name, json
		if _.isObject json
			mesh.scale.x = json.scale
			mesh.scale.y = json.scale
			mesh.scale.z = json.scale
		mesh.geometry.computeBoundingBox()
		return mesh

return ViewerUI