BaseView = require 'views/baseView'
templates = require 'templates'

class WorldProperties extends BaseView
	
	id: 'sidebar-properties-world'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/worldProperties.tmpl'
	
return WorldProperties