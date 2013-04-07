BaseView = require 'views/baseView'
templates = require 'templates'

class ObjectProperties extends BaseView
	
	id: 'sidebar-properties-object'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/objectProperties.tmpl'
	
return ObjectProperties