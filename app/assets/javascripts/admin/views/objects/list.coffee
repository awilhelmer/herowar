AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class ObjectList extends AdminAuthView

	entity: 'api/objects'

	id: 'object-list'
	
	template: templates.get 'objects/list.tmpl'

return ObjectList