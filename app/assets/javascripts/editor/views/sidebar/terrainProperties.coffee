EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
Constants = require 'constants'

class TerrainProperties extends BasePropertiesView
	
	id: 'sidebar-properties-terrain'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/terrainProperties.tmpl'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @showPanel
		EditorEventbus.showObjectProperties.add @hidePanel

	createSliders: ->
		@createSlider @$el.find('#inputWidth').get(0), Constants.TERRAIN_MIN_WIDTH, Constants.TERRAIN_MAX_WIDTH, Constants.TERRAIN_STEPS_WIDTH, @changeTerrain
		@createSlider @$el.find('#inputHeight').get(0), Constants.TERRAIN_MIN_HEIGHT, Constants.TERRAIN_MAX_HEIGHT, Constants.TERRAIN_STEPS_HEIGHT, @changeTerrain
		@createSlider @$el.find('#inputSmoothness').get(0), Constants.TERRAIN_MIN_SMOOTHNESS, Constants.TERRAIN_MAX_SMOOTHNESS, Constants.TERRAIN_STEPS_SMOOTHNESS, @changeTerrain
		@createSlider @$el.find('#inputZScale').get(0), Constants.TERRAIN_MIN_ZSCALE, Constants.TERRAIN_MAX_ZSCALE, Constants.TERRAIN_STEPS_ZSCALE, @changeTerrain


	changeTerrain: (event) =>
		console.log 'CHANGE TERRAIN !!!!'

return TerrainProperties