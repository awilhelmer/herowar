AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class NewsList extends AdminAuthView

	entity: 'api/newss'

	id: 'news-list'
	
	template: templates.get 'news/list.tmpl'

return NewsList