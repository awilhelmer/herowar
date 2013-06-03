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
		$currentTarget = $ event.currentTarget
		towerId = $currentTarget.data 'id'
		events.trigger 'select:tower', towerId

return BuildView