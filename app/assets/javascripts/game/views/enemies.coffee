BaseView = require 'views/baseView'
templates = require 'templates'

class EnemiesView extends BaseView

	id: 'enemies'
	
	entity: 'ui/enemies'
	
	template: templates.get 'enemies.tmpl'
	
return EnemiesView