BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarTerrainEditView extends BaseView
	
	template: templates.get 'scenebar/terrainEdit.tmpl'

	events:
		'click #editor-menubar-brush-materials' : 'materials'
		'click #editor-menubar-brush-raise' 		: 'raise'
		'click #editor-menubar-brush-degrade' 	: 'degrade'

	dummy: (event) =>
		event?.preventDefault()

	materials: (event) =>
		unless event then return
		event.preventDefault()
		$('#editor-menubar-brush a').removeClass 'active'
		$(event.currentTarget).addClass 'active'
		Constants.TOOL_BRUSH_TYPE = Constants.BRUSH_APPLY_MATERIAL
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_APPLY_MATERIAL

	raise: (event) =>
		unless event then return
		event.preventDefault()
		$('#editor-menubar-brush a').removeClass 'active'
		$(event.currentTarget).addClass 'active'
		Constants.TOOL_BRUSH_TYPE = Constants.BRUSH_TERRAIN_RAISE
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_TERRAIN_RAISE

	degrade: (event) =>
		unless event then return
		event.preventDefault()
		$('#editor-menubar-brush a').removeClass 'active'
		$(event.currentTarget).addClass 'active'
		Constants.TOOL_BRUSH_TYPE = Constants.BRUSH_TERRAIN_DEGRADE
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_TERRAIN_DEGRADE

return ScenebarTerrainEditView