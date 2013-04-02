TableView = require 'views/tableView'
app = require 'application'

class ObjectTable extends TableView

	entity: 'api/objects'
	
	tableEntity: 'db/objects'
	
	fields:
		'#'						: 'id'
		'Name' 				: 'name'
		'Description'	: 'description'

	entryView: 'views/objects/tableEntry'

	createEntry: (event) ->
		event?.preventDefault()
		app.navigate "admin/object/new", true 

return ObjectTable