BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarTerrainView extends BaseView
	
	template: templates.get 'scenebar/terrain.tmpl'

return ScenebarTerrainView