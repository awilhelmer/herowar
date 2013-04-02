AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class ObjectEdit extends AdminAuthView

	entity: 'db/objects'
	
	template: templates.get 'objects/edit.tmpl'

return ObjectEdit