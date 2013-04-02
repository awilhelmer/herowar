TableView = require 'views/tableView'
app = require 'application'

class NewsTable extends TableView

	entity: 'api/newss'
	
	tableEntity: 'db/newss'
	
	fields:
		'#'						: 'id'
		'Headline'		: 'headline'
		'Text'				: 'text'

	entryView: 'views/news/tableEntry'

	createEntry: (event) ->
		event?.preventDefault()
		app.navigate "admin/news/new", true 

return NewsTable