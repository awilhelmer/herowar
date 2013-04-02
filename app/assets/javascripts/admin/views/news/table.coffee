TableView = require 'views/tableView'
app = require 'application'

class NewsTable extends TableView

	entity: 'api/objects'
	
	tableEntity: 'db/objects'
	
	fields:
		'#'						: 'id'
		'Headline'		: 'headline'
		'Text'				: 'text'

	createEntry: (event) ->
		event?.preventDefault()
		app.navigate "admin/news/new", true 

return NewsTable