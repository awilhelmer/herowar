BaseView = require 'views/baseView'
EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
templates = require 'templates'

class IconbarView extends BaseView

	id: 'iconbar'
	
	template: templates.get 'iconbar.tmpl'
	
	events:
		'click #editor-menubar-back' : 'back'
		'click #editor-menubar-into' : 'into'
		'click #editor-menubar-tool-selection' : 'select'
		'click #editor-menubar-tool-brush' : 'brush'

	back: (event) ->
		event?.preventDefault()

	into: (event) ->
		event?.preventDefault()

	select: (event) =>
		if event
			event.preventDefault()
			$('#editor-menubar-tools a').removeClass 'active'
			$(event.currentTarget).addClass 'active'
			EditorEventbus.selectTool.dispatch Constants.TOOL_SELECTION

	brush: (event) =>
		if event
			event.preventDefault()
			$('#editor-menubar-tools a').removeClass 'active'
			$(event.currentTarget).addClass 'active'
			EditorEventbus.selectTool.dispatch Constants.TOOL_BRUSH

return IconbarView