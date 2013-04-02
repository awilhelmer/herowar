AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class ObjectNew extends AdminAuthView

	id: 'object-new'
	
	template: templates.get 'objects/new.tmpl'

return ObjectNew