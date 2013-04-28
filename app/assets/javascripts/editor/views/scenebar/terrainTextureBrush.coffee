BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarTerrainTextureBrushView extends BaseView
	
	template: templates.get 'scenebar/terrainTextureBrush.tmpl'

	events:
		'click #scenebar-terrain-brush-small' 	: 'dummy'
		'click #scenebar-terrain-brush-medium'	: 'dummy'
		'click #scenebar-terrain-brush-large' 	: 'dummy'

	dummy: (event) =>
		event?.preventDefault()

return ScenebarTerrainTextureBrushView