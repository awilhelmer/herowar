BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarTerrainTextureBrushView extends BaseView
	
	template: templates.get 'scenebar/terrainTextureBrush.tmpl'

	events:
		'click #scenebar-terrain-brush-small' 	: 'brushSizeTiny'
		'click #scenebar-terrain-brush-medium'	: 'brushSizeMedium'
		'click #scenebar-terrain-brush-large' 	: 'brushSizeLarge'

	dummy: (event) =>
		event?.preventDefault()

	brushSizeTiny: (event) =>
		unless event then return
		event.preventDefault()
		Constants.TOOL_BRUSH_SIZE = Constants.BRUSH_SIZE_TINY
		EditorEventbus.selectBrushSize.dispatch Constants.BRUSH_SIZE_TINY
		@render()

	brushSizeMedium: (event) =>
		unless event then return
		event.preventDefault()
		Constants.TOOL_BRUSH_SIZE = Constants.BRUSH_SIZE_MEDIUM
		EditorEventbus.selectBrushSize.dispatch Constants.BRUSH_SIZE_MEDIUM
		@render()

	brushSizeLarge: (event) =>
		unless event then return
		event.preventDefault()
		Constants.TOOL_BRUSH_SIZE = Constants.BRUSH_SIZE_LARGE
		EditorEventbus.selectBrushSize.dispatch Constants.BRUSH_SIZE_LARGE
		@render()

return ScenebarTerrainTextureBrushView