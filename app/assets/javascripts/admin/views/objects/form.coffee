EditFormView = require 'views/editFormView'
templates = require 'templates'
app = require 'application'

class ObjectForm extends EditFormView
	
	entity: 'db/objects'
	
	entityType: 'object'
	
	template: templates.get 'objects/form.tmpl'

return ObjectForm