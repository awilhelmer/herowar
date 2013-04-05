BasePanel = require 'ui/panel/basePanel'
ObjectHelper = require 'helper/objectHelper'
RandomPool = require 'helper/randomPool'
TerrainModel = require 'model/terrain'
Constants = require 'constants'

class TerrainPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-terrain'

	initialize: ->
		console.log 'Initialize editor terrain properties'
		@randomPool = new RandomPool()
		@randomPool.hook()
		@objectHelper = new ObjectHelper @app
		@terrainWidth = Constants.TERRAIN_DEFAULT_WIDTH
		@terrainHeight = Constants.TERRAIN_DEFAULT_HEIGHT
		@terrainSmoothness = Constants.TERRAIN_DEFAULT_SMOOTHNESS
		@terrainZScale = Constants.TERRAIN_DEFAULT_ZSCALE
		@wireframe = true
		@model = new TerrainModel()
		@buildTerrain()
		@removeSelectionWireframe()
		super()

	bindEvents: ->
		@$container.on 'keyup', 'input[name="width"],input[name="height"],input[name="smoothness"],input[name="zScale"]', @changeTerrain
		@$container.on 'change', 'input[name="width"]', @validateTerrainWidth
		@$container.on 'change', 'input[name="height"]', @validateTerrainHeight
		@$container.on 'change', 'input[name="wireframe"]', @changeWireframe
		@$container.on 'click', '#randomizeTerrain', @resetTerrainPool
	
	createSliders: ->
		@createSlider @$container.find('#inputWidth').get(0), Constants.TERRAIN_MIN_WIDTH, Constants.TERRAIN_MAX_WIDTH, Constants.TERRAIN_STEPS_WIDTH, @changeTerrain
		@createSlider @$container.find('#inputHeight').get(0), Constants.TERRAIN_MIN_HEIGHT, Constants.TERRAIN_MAX_HEIGHT, Constants.TERRAIN_STEPS_HEIGHT, @changeTerrain
		@createSlider @$container.find('#inputSmoothness').get(0), Constants.TERRAIN_MIN_SMOOTHNESS, Constants.TERRAIN_MAX_SMOOTHNESS, Constants.TERRAIN_STEPS_SMOOTHNESS, @changeTerrain
		@createSlider @$container.find('#inputZScale').get(0), Constants.TERRAIN_MIN_ZSCALE, Constants.TERRAIN_MAX_ZSCALE, Constants.TERRAIN_STEPS_ZSCALE, @changeTerrain

	buildTerrain: ->
		console.log "Change terrain: size=#{@terrainWidth}x#{@terrainHeight} smoothness=#{@terrainSmoothness} zscale=#{@terrainZScale}"
		@randomPool.seek 0
		map = @model.update @terrainWidth, @terrainHeight, @terrainSmoothness, @terrainZScale
		@app.scenegraph().setMap map
		@addSelectionWireframe()
		@app.render()

	changeTerrain: (event) =>
		width = @$container.find('input[name="width"]').val()
		height = @$container.find('input[name="height"]').val()
		smoothness = @$container.find('input[name="smoothness"]').val()
		zScale = @$container.find('input[name="zScale"]').val()
		if @hasValidSize(width, height) and @hasChangedSize(parseInt(width), parseInt(height), parseFloat(smoothness), parseInt(zScale))
			@terrainWidth = width
			@terrainHeight = height
			@terrainSmoothness = smoothness
			@terrainZScale = zScale
			@buildTerrain()

	resetTerrainPool: =>
		@randomPool.reset()
		@buildTerrain()

	validateTerrainWidth: (event) =>
		if event
			$currentTarget = $ event.currentTarget
			val = $currentTarget.val()
			if val <= Constants.TERRAIN_MIN_WIDTH or val >= Constants.TERRAIN_MAX_WIDTH
				$currentTarget.val @terrainWidth 
				fdSlider.updateSlider $currentTarget.attr 'id'
			
	validateTerrainHeight: (event) =>
		if event
			$currentTarget = $ event.currentTarget
			val = $currentTarget.val()
			if val <= Constants.TERRAIN_MIN_HEIGHT or val >= Constants.TERRAIN_MAX_HEIGHT
				$currentTarget.val @terrainHeight 
				fdSlider.updateSlider $currentTarget.attr 'id'

	hasValidSize: (width, height) ->
		width >= Constants.TERRAIN_MIN_WIDTH and width <= Constants.TERRAIN_MAX_WIDTH and height >= Constants.TERRAIN_MIN_HEIGHT and height <= Constants.TERRAIN_MAX_HEIGHT

	hasChangedSize: (width, height, smoothness, zScale) ->
		width != @terrainWidth or height != @terrainHeight or smoothness != @terrainSmoothness or zScale != @terrainZScale

	changeWireframe: (event) =>
		if event
			map = @app.scenegraph().getMap()
			$currentTarget = $ event.currentTarget
			@wireframe = $currentTarget.is ':checked'

	addSelectionWireframe: ->
		map = @app.scenegraph().getMap()
		if @objectHelper.hasWireframe map
			@objectHelper.changeWireframeColor map, 0xFFFF00
		else
			@objectHelper.addWireframe map, 0xFFFF00

	removeSelectionWireframe: ->
		map = @app.scenegraph().getMap()
		if @wireframe and @objectHelper.hasWireframe map
			@objectHelper.changeWireframeColor map, 0xFFFFFF
		else
			@objectHelper.removeWireframe map

return TerrainPropertiesPanel