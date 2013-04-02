TableEntryView = require 'views/tableEntryView'

class NewsTableEntry extends TableEntryView

	entityType: 'news'
	
	deleteField: 'headline'

return NewsTableEntry