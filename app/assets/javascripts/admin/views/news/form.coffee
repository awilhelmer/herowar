EditFormView = require 'views/editFormView'
templates = require 'templates'
app = require 'application'

class NewsForm extends EditFormView
	
	entity: 'db/newss'
	
	entityType: 'news'
	
	template: templates.get 'news/form.tmpl'

return NewsForm