BaseView = require 'views/baseView'
templates = require 'templates'

class ProgressView extends BaseView

	id: 'progress'
	
	template: templates.get 'progress.tmpl'
	
return ProgressView