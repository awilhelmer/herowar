BaseView = require 'views/baseView'
templates = require 'templates'

class BuildView extends BaseView

	id: 'build'
		
	template: templates.get 'build.tmpl'
	
return BuildView