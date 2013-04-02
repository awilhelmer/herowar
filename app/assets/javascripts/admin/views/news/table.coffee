TableView = require 'views/tableView'
app = require 'application'

class NewsTable extends TableView

	entity: 'api/news'
	
	tableEntity: 'db/newss'
	
	fields:
		'#'						: 'id'
		'Headline'		: 'headline'
		'Text'				: 'text'

	createEntry: (event) ->
		event?.preventDefault()
		app.navigate "admin/news/new", true 

return NewsTable