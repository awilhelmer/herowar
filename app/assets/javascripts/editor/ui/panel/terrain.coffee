BasePanel = require 'ui/panel/basePanel'
ObjectHelper = require 'helper/objectHelper'

class TerrainPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-terrain'

	initialize: ->
		console.log 'Initialize editor terrain properties'
		@objectHelper = new ObjectHelper @app
		@terrainWidth = 2000
		@terrainHeight = 2000
		@terrainSmoothness = 0.1
		@terrainZScale = 0
		@wireframe = true
		super()

	bindEvents: ->
		@$container.on 'keyup', 'input[name="width"],input[name="height"]', @changeTerrain
		@$container.on 'change', 'input[name="width"]', @validateTerrainWidth
		@$container.on 'change', 'input[name="height"]', @validateTerrainHeight
		@$container.on 'change', 'input[name="wireframe"]', @changeWireframe
	
	createSliders: ->
		@createSlider @$container.find('#inputWidth').get(0), 200, 10000, 10, @changeTerrain
		@createSlider @$container.find('#inputHeight').get(0), 200, 10000, 10, @changeTerrain
		@createSlider @$container.find('#inputSmoothness').get(0), 0.1, 5, 0.1, @changeTerrain
		@createSlider @$container.find('#inputZScale').get(0), 0, 500, 20, @changeTerrain
	
	changeTerrain: (event) =>
		width = @$container.find('input[name="width"]').val()
		height = @$container.find('input[name="height"]').val()
		if @hasValidSize(width, height) and @hasChangedSize(parseInt(width), parseInt(height))
			console.log "Change terrain: size=#{width}x#{height} smoothness=#{@terrainSmoothness} zscale=#{@terrainZScale}"
			@app.scenegraph().setMap @app.scenegraph().createDefaultMap width, height
			@addSelectionWireframe()
			@terrainWidth = width
			@terrainHeight = height
			@app.render()

	validateTerrainWidth: (event) =>
		if event
			$currentTarget = $ event.currentTarget
			val = $currentTarget.val()
			if val <= 200 or val >= 10000
				$currentTarget.val @terrainWidth 
				fdSlider.updateSlider $currentTarget.attr 'id'
			
	validateTerrainHeight: (event) =>
		if event
			$currentTarget = $ event.currentTarget
			val = $currentTarget.val()
			if val <= 200 or val >= 10000
				$currentTarget.val @terrainHeight 
				fdSlider.updateSlider $currentTarget.attr 'id'

	hasValidSize: (width, height) ->
		width >= 200 and width <= 10000 and height >= 200 and height <= 10000

	hasChangedSize: (width, height) ->
		width != @terrainWidth or height != @terrainHeight

	changeWireframe: (event) =>
		if event
			map = @app.scenegraph().getMap()
			$currentTarget = $ event.currentTarget
			@wireframe = $currentTarget.is ':checked'

	addSelectionWireframe: ->
		map = @app.scenegraph().getMap()
		if @wireframe
			@objectHelper.changeWireframeColor map, 0xFFFF00
		else
			@objectHelper.addWireframe map, 0xFFFF00

	removeSelectionWireframe: ->
		map = @app.scenegraph().getMap()
		if @wireframe
			@objectHelper.changeWireframeColor map, 0xFFFFFF
		else
			@objectHelper.removeWireframe map

return TerrainPropertiesPanel