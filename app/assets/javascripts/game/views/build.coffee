BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'

class BuildView extends BaseView

	id: 'build'
		
	template: templates.get 'build.tmpl'

	entity: 'db/towers'

	events:
		'click .item' : 'selectTower'
	
	selectTower: (event) ->
		unless event then return
		towerId = 1 # TODO: this should not be hardcoded...
		events.trigger 'select:tower', towerId

return BuildView