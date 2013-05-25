BaseView = require 'views/baseView'
templates = require 'templates'

class StatsView extends BaseView

	id: 'stats'
		
	template: templates.get 'stats.tmpl'
	
return StatsView