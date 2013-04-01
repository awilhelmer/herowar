TableEntryView = require 'views/tableEntryView'
templates = require 'templates'
app = require 'application'

###
    The MapsTableEntry shows a row in the map table.

    @author Sebastian Sachtleben
###
class MapsTableEntry extends TableEntryView

	template: templates.get 'maps/tableEntry.tmpl'

	events:
		'click .edit-link'		: 'editEntry'
		'click .delete-link'	: 'deleteEntry'
		'click .editor-link'	: 'editorEntry'

	editEntry: (event) ->
		event?.preventDefault()
		console.log "Edit #{@model.id}"
		app.navigate "admin/map/#{@model.id}", true 
		
	deleteEntry: (event) ->
		event?.preventDefault()
		console.log "Delete entry #{@model.id}"

	editorEntry: (event) ->
		event?.preventDefault()
		console.log "Jump in editor with #{@model.id}"

return MapsTableEntry