TableEntryView = require 'views/tableEntryView'
templates = require 'templates'
app = require 'application'
db = require 'database'

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
		console.log @model
		if confirm("Do you really want to delete \"#{@model.get('name')}\"?")
			console.log "Delete entry #{@model.id}"
			$.ajax
				type: 'DELETE'
				url: "#{app.resourcePath()}map/#{@model.id}"
				success: (data, textStatus, jqXHR) =>
					$.gritter.add
						title: 'Delete Map',
						text: "The map \"#{@model.get('name')}\" has been successfully deleted."
					maps = db.get 'api/maps'
					maps.fetch()

	editorEntry: (event) ->
		event?.preventDefault()
		console.log "Jump in editor with #{@model.id}"

return MapsTableEntry