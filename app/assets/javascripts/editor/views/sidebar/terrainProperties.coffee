EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class TerrainProperties extends BasePropertiesView
	
	id: 'sidebar-properties-terrain'
	
	className: 'sidebar-panel hidden'
	
	entity: 'world'
	
	template: templates.get 'sidebar/terrainProperties.tmpl'
	
	events:
		'keyup input[name="width"]' 			: 'changeTerrain'
		'keyup input[name="height"]' 			: 'changeTerrain'
		'keyup input[name="smoothness"]' 	: 'changeTerrain'
		'keyup input[name="zScale"]' 			: 'changeTerrain'
		'change input[name="width"]' 			: 'validateTerrainWidth'
		'change input[name="height"]' 		: 'validateTerrainHeight'
		'change input[name="wireframe"]' 	: 'changeWireframe'
		'click #randomizeTerrain' 				: 'resetTerrainPool'
		'click .mm-material' 							: 'loadMaterial'

	initialize: (options) ->
		@sidebar = db.get 'ui/sidebar'
		@terrain = db.get 'ui/terrain'
		super options

	bindEvents: ->
		@listenTo @model, 'change:materials', @render if @model
		@listenTo @sidebar, 'change:active', @changeActiveView
		return

	createSliders: ->
		@createSlider @$el.find('#inputWidth').get(0), Constants.TERRAIN_MIN_WIDTH, Constants.TERRAIN_MAX_WIDTH, Constants.TERRAIN_STEPS_WIDTH, @changeTerrain
		@createSlider @$el.find('#inputHeight').get(0), Constants.TERRAIN_MIN_HEIGHT, Constants.TERRAIN_MAX_HEIGHT, Constants.TERRAIN_STEPS_HEIGHT, @changeTerrain
		@createSlider @$el.find('#inputSmoothness').get(0), Constants.TERRAIN_MIN_SMOOTHNESS, Constants.TERRAIN_MAX_SMOOTHNESS, Constants.TERRAIN_STEPS_SMOOTHNESS, @changeTerrain
		@createSlider @$el.find('#inputZScale').get(0), Constants.TERRAIN_MIN_ZSCALE, Constants.TERRAIN_MAX_ZSCALE, Constants.TERRAIN_STEPS_ZSCALE, @changeTerrain
		return

	changeTerrain: (event) =>
		log.debug 'CHANGE TERRAIN !!!!'
		EditorEventbus.dispatch 'changeTerrain', @$el.find('input[name="width"]').val(), @$el.find('input[name="height"]').val(), @$el.find('input[name="smoothness"]').val(), @$el.find('input[name="zScale"]').val()
		return

	validateTerrainWidth: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		val = $currentTarget.val()
		if val <= Constants.TERRAIN_MIN_WIDTH or val >= Constants.TERRAIN_MAX_WIDTH
			$currentTarget.val @terrainWidth 
			fdSlider.updateSlider $currentTarget.attr 'id'
		return
			
	validateTerrainHeight: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		val = $currentTarget.val()
		if val <= Constants.TERRAIN_MIN_HEIGHT or val >= Constants.TERRAIN_MAX_HEIGHT
			$currentTarget.val @terrainHeight 
			fdSlider.updateSlider $currentTarget.attr 'id'
		return

	changeWireframe: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		EditorEventbus.dispatch 'changeTerrainWireframe', $currentTarget.is ':checked'
		return

	resetTerrainPool: =>
		EditorEventbus.dispatch 'resetTerrainPool'
		return

	loadMaterial: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		if modelId
			@terrain.set 'brushMaterialId', modelId
			@sidebar.set 'active', 'sidebar-properties-material'
		return

return TerrainProperties