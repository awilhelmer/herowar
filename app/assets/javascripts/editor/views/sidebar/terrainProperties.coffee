EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
Constants = require 'constants'

class TerrainProperties extends BasePropertiesView
	
	id: 'sidebar-properties-terrain'
	
	className: 'sidebar-panel hidden'
	
	entity: 'terrain'
	
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

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @showPanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		@listenTo @model, 'change:materials', @render if @model

	createSliders: ->
		@createSlider @$el.find('#inputWidth').get(0), Constants.TERRAIN_MIN_WIDTH, Constants.TERRAIN_MAX_WIDTH, Constants.TERRAIN_STEPS_WIDTH, @changeTerrain
		@createSlider @$el.find('#inputHeight').get(0), Constants.TERRAIN_MIN_HEIGHT, Constants.TERRAIN_MAX_HEIGHT, Constants.TERRAIN_STEPS_HEIGHT, @changeTerrain
		@createSlider @$el.find('#inputSmoothness').get(0), Constants.TERRAIN_MIN_SMOOTHNESS, Constants.TERRAIN_MAX_SMOOTHNESS, Constants.TERRAIN_STEPS_SMOOTHNESS, @changeTerrain
		@createSlider @$el.find('#inputZScale').get(0), Constants.TERRAIN_MIN_ZSCALE, Constants.TERRAIN_MAX_ZSCALE, Constants.TERRAIN_STEPS_ZSCALE, @changeTerrain

	changeTerrain: (event) =>
		console.log 'CHANGE TERRAIN !!!!'
		width = @$el.find('input[name="width"]').val()
		height = @$el.find('input[name="height"]').val()
		smoothness = @$el.find('input[name="smoothness"]').val()
		zScale = @$el.find('input[name="zScale"]').val()
		EditorEventbus.changeTerrain.dispatch width, height, smoothness, zScale

	validateTerrainWidth: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		val = $currentTarget.val()
		if val <= Constants.TERRAIN_MIN_WIDTH or val >= Constants.TERRAIN_MAX_WIDTH
			$currentTarget.val @terrainWidth 
			fdSlider.updateSlider $currentTarget.attr 'id'
			
	validateTerrainHeight: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		val = $currentTarget.val()
		if val <= Constants.TERRAIN_MIN_HEIGHT or val >= Constants.TERRAIN_MAX_HEIGHT
			$currentTarget.val @terrainHeight 
			fdSlider.updateSlider $currentTarget.attr 'id'

	changeWireframe: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		EditorEventbus.changeTerrainWireframe.dispatch $currentTarget.is ':checked'

	resetTerrainPool: =>
		EditorEventbus.resetTerrainPool.dispatch()

	getTemplateData: ->
		json = super()
		materials = []
		for material in json.materials
			materials.push material.toJSON()
		json.materials = materials
		json

return TerrainProperties