EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
Constants = require 'constants'
templates = require 'templates'

class ScenebarTerrainTextureBrushView extends BaseView
	
	entity: 'ui/terrain'
	
	template: templates.get 'scenebar/terrainTextureBrush.tmpl'

	events:
		'click #scenebar-terrain-brush-small' 	: 'brushSizeTiny'
		'click #scenebar-terrain-brush-medium'	: 'brushSizeMedium'
		'click #scenebar-terrain-brush-large' 	: 'brushSizeLarge'

	initialize: (options) ->
		super options
		@model.set Constants.BRUSH_SIZE, Constants.BRUSH_SIZE_TINY

	brushSizeTiny: (event) =>
		unless event then return
		event.preventDefault()
		@model.set Constants.BRUSH_SIZE, Constants.BRUSH_SIZE_TINY
		EditorEventbus.dispatch 'selectBrushSize', Constants.BRUSH_SIZE_TINY
		@render()

	brushSizeMedium: (event) =>
		unless event then return
		event.preventDefault()
		@model.set Constants.BRUSH_SIZE, Constants.BRUSH_SIZE_MEDIUM
		EditorEventbus.dispatch 'selectBrushSize', Constants.BRUSH_SIZE_MEDIUM
		@render()

	brushSizeLarge: (event) =>
		unless event then return
		event.preventDefault()
		@model.set Constants.BRUSH_SIZE, Constants.BRUSH_SIZE_LARGE
		EditorEventbus.dispatch 'selectBrushSize', Constants.BRUSH_SIZE_LARGE
		@render()
		
	getTemplateData: ->
		json = super()
		json.isActive = @model.get(Constants.BRUSH_MODE) is Constants.BRUSH_APPLY_MATERIAL
		json.isSelectedTiny = @model.get(Constants.BRUSH_SIZE) is Constants.BRUSH_SIZE_TINY
		json.isSelectedMedium = @model.get(Constants.BRUSH_SIZE) is Constants.BRUSH_SIZE_MEDIUM
		json.isSelectedLarge = @model.get(Constants.BRUSH_SIZE) is Constants.BRUSH_SIZE_LARGE
		json

return ScenebarTerrainTextureBrushView