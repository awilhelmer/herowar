TableView = require 'views/tableView'

###
    The MapsTable shows a list of maps.

    @author Sebastian Sachtleben
###
class MapsTable extends TableView

	entity: 'api/maps'
	
	tableEntity: 'db/maps'
	
	fields:
		'#'						: 'id'
		'Name' 				: 'name'
		'Description'	: 'description'
		'Team Size'		: 'teamSize'
		
	entryView: 'views/maps/tableEntry'

	createEntry: (event) ->
		event?.preventDefault()
		console.log 'Create new entry'

return MapsTable