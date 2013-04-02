TableEntryView = require 'views/tableEntryView'
templates = require 'templates'
app = require 'application'
db = require 'database'

class MapTableEntry extends TableEntryView

	template: templates.get 'maps/tableEntry.tmpl'

	entityType: 'map'

	events:
		'click .edit-link'		: 'editEntry'
		'click .delete-link'	: 'deleteEntry'
		'click .editor-link'	: 'editorEntry'

	editorEntry: (event) ->
		event?.preventDefault()
		console.log "Jump in editor with map \"#{@model.get('name')}\""

return MapTableEntry