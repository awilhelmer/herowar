AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class NewsNew extends AdminAuthView

	id: 'news-new'
	
	template: templates.get 'news/new.tmpl'

return NewsNew