BaseView = require 'views/baseView'
EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
templates = require 'templates'

class IconbarView extends BaseView

    id: 'iconbar'
	
    template: templates.get 'iconbar.tmpl'
	
    events:
        'click #editor-menubar-back'            : 'back'
        'click #editor-menubar-into'            : 'into'
        'click #editor-menubar-tool-selection'  : 'select'
        'click #editor-menubar-tool-brush'      : 'brush'
        'click #editor-menubar-brush-materials' : 'materials'
        'click #editor-menubar-brush-raise'     : 'raise'
        'click #editor-menubar-brush-degrade'   : 'degrade'

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
        EditorEventbus.selectTool.dispatch Constants.TOOL_SELECTION

    brush: (event) =>
        unless event then return
        event.preventDefault()
        $('#editor-menubar-tools a').removeClass 'active'
        $('#editor-menubar-brush a').removeClass('disabled').first().addClass 'active'
        $(event.currentTarget).addClass 'active'
        EditorEventbus.selectTool.dispatch Constants.TOOL_BRUSH

    materials: (event) =>
        unless event then return
        event.preventDefault()
        $('#editor-menubar-brush a').removeClass 'active'
        $(event.currentTarget).addClass 'active'

    raise: (event) =>
        unless event then return
        event.preventDefault()
        $('#editor-menubar-brush a').removeClass 'active'
        $(event.currentTarget).addClass 'active'

    degrade: (event) =>
        unless event then return
        event.preventDefault()
        $('#editor-menubar-brush a').removeClass 'active'
        $(event.currentTarget).addClass 'active'

return IconbarView