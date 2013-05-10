BaseView = require 'views/baseView'
templates = require 'templates'

class WavesView extends BaseView

	id: 'waves'
	
	entity: 'ui/waves'
	
	template: templates.get 'waves.tmpl'
	
return WavesView