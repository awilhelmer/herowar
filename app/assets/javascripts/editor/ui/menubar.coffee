BaseElement = require 'ui/baseElement'
Constants = require 'constants'
	
class Menubar extends BaseElement

	constructor: (@editor) ->
		super @editor, 'editor-menubar'

	initialize: ->
		console.log 'Initialize menubar'

	bindEvents: ->
		@$container.on 'click', '#editor-menubar-back', @onBack
		@$container.on 'click', '#editor-menubar-into', @onInto
		@$container.on 'click', '#editor-menubar-tool-selection', @onToolSelection
		@$container.on 'click', '#editor-menubar-tool-brush', @onToolBrush
	
	onBack: (event) ->
		event?.preventDefault()

	onInto: (event) ->
		event?.preventDefault()

	onToolSelection: (event) =>
		if event
			event.preventDefault()
			$('#editor-menubar-tools a').removeClass 'active'
			$(event.currentTarget).addClass 'active'
			@editor.tool = Constants.TOOL_SELECTION

	onToolBrush: (event) =>
		if event
			event.preventDefault()
			$('#editor-menubar-tools a').removeClass 'active'
			$(event.currentTarget).addClass 'active'
			@editor.tool = Constants.TOOL_BRUSH

return Menubar