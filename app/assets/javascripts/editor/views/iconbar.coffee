BaseView = require 'views/baseView'
EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
templates = require 'templates'

class IconbarView extends BaseView

	id: 'iconbar'
	
	template: templates.get 'iconbar.tmpl'
	
	events:
		'click #editor-menubar-back'						: 'back'
		'click #editor-menubar-into'						: 'into'
		'click #editor-menubar-tool-selection'	: 'select'
		'click #editor-menubar-tool-brush'			: 'brush'
		'click #editor-menubar-brush-tiny'			:	'brushSizeTiny'
		'click #editor-menubar-brush-medium'		:	'brushSizeMedium'
		'click #editor-menubar-brush-large'			:	'brushSizeLarge'
		'click #editor-menubar-brush-materials' : 'materials'
		'click #editor-menubar-brush-raise'		 	: 'raise'
		'click #editor-menubar-brush-degrade'	 	: 'degrade'

	back: (event) ->
		event?.preventDefault()

	into: (event) ->
		event?.preventDefault()

	select: (event) =>
		unless event then return
		event.preventDefault()
		$('#editor-menubar-tools a').removeClass 'active'
		$('#editor-menubar-brush a').removeClass('active').addClass 'disabled'
		$(event.currentTarget).addClass 'active'
		Constants.TOOL_BRUSH_SELECTED = false
		console.log 'Set Tool Selection'
		EditorEventbus.selectTool.dispatch Constants.TOOL_SELECTION

	brush: (event) =>
		unless event then return
		event.preventDefault()
		$('#editor-menubar-tools a').removeClass 'active'
		$('#editor-menubar-brush a').removeClass('disabled').first().addClass 'active'
		$(event.currentTarget).addClass 'active'
		Constants.TOOL_BRUSH_SELECTED = true
		console.log 'Set Brush Selection'
		EditorEventbus.selectTool.dispatch Constants.TOOL_BRUSH
		EditorEventbus.selectBrush.dispatch Constants.BRUSH_APPLY_MATERIAL

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

	getTemplateData: ->
		json = {}
		json.selectIsSelected = !Constants.TOOL_BRUSH_SELECTED
		json.brushIsSelected = Constants.TOOL_BRUSH_SELECTED
		json.brushIsTiny = Constants.TOOL_BRUSH_SIZE is Constants.BRUSH_SIZE_TINY
		json.brushIsMedium = Constants.TOOL_BRUSH_SIZE is Constants.BRUSH_SIZE_MEDIUM
		json.brushIsLarge = Constants.TOOL_BRUSH_SIZE is Constants.BRUSH_SIZE_LARGE
		json.brushTypeIsMaterial = Constants.TOOL_BRUSH_TYPE is Constants.BRUSH_APPLY_MATERIAL
		json.brushTypeIsTerrainRaise = Constants.TOOL_BRUSH_TYPE is Constants.BRUSH_TERRAIN_RAISE
		json.brushTypeIsTerrainDegrade = Constants.TOOL_BRUSH_TYPE is Constants.BRUSH_TERRAIN_DEGRADE
		json

return IconbarView