BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarTerrainEditView extends BaseView
	
	template: templates.get 'scenebar/terrainEdit.tmpl'

	events:
		'click #editor-menubar-brush-materials' : 'dummy'
		'click #editor-menubar-brush-raise' 		: 'dummy'
		'click #editor-menubar-brush-degrade' 	: 'dummy'

	dummy: (event) =>
		event?.preventDefault()

return ScenebarTerrainEditView