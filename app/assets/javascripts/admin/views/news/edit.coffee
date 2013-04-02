AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class NewsEdit extends AdminAuthView

	entity: 'db/newss'
	
	template: templates.get 'news/edit.tmpl'

return NewsEdit