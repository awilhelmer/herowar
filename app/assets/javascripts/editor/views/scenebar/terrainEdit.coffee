EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
Constants = require 'constants'
templates = require 'templates'

class ScenebarTerrainEditView extends BaseView

	entity: 'ui/terrain'
	
	template: templates.get 'scenebar/terrainEdit.tmpl'

	events:
		'click #editor-menubar-brush-materials' : 'materials'
		'click #editor-menubar-brush-raise' 		: 'raise'
		'click #editor-menubar-brush-degrade' 	: 'degrade'

	materials: (event) =>
		unless event then return
		event.preventDefault()
		$('#scenebar-terrain-edit a').removeClass 'active'
		$(event.currentTarget).addClass 'active'
		@model.set Constants.BRUSH_MODE, Constants.BRUSH_APPLY_MATERIAL
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_APPLY_MATERIAL

	raise: (event) =>
		unless event then return
		event.preventDefault()
		$('#scenebar-terrain-edit a').removeClass 'active'
		$(event.currentTarget).addClass 'active'
		@model.set Constants.BRUSH_MODE, Constants.BRUSH_TERRAIN_RAISE
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_TERRAIN_RAISE

	degrade: (event) =>
		unless event then return
		event.preventDefault()
		$('#scenebar-terrain-edit a').removeClass 'active'
		$(event.currentTarget).addClass 'active'
		@model.set Constants.BRUSH_MODE, Constants.BRUSH_TERRAIN_DEGRADE
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_TERRAIN_DEGRADE

	getTemplateData: ->
		json = super()
		json.isSelectedMaterial = @model.get(Constants.BRUSH_MODE) is Constants.BRUSH_APPLY_MATERIAL
		json.isSelectedRaise = @model.get(Constants.BRUSH_MODE) is Constants.BRUSH_TERRAIN_RAISE
		json.isSelectedDegrade = @model.get(Constants.BRUSH_MODE) is Constants.BRUSH_TERRAIN_DEGRADE
		json

return ScenebarTerrainEditView