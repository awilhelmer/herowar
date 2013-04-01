TableEntryView = require 'views/tableEntryView'
templates = require 'templates'
app = require 'application'
db = require 'database'

class MapsTableEntry extends TableEntryView

	template: templates.get 'maps/tableEntry.tmpl'

	entityType: 'map'

	events:
		'click .edit-link'		: 'editEntry'
		'click .delete-link'	: 'deleteEntry'
		'click .editor-link'	: 'editorEntry'

	editEntry: (event) ->
		event?.preventDefault()
		console.log "Edit #{@model.id}"
		app.navigate "admin/map/#{@model.id}", true 

	editorEntry: (event) ->
		event?.preventDefault()
		console.log "Jump in editor with #{@model.id}"

return MapsTableEntry