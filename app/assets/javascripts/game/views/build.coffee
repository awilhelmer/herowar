BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'

class BuildView extends BaseView

	id: 'build'
		
	template: templates.get 'build.tmpl'

	entity: 'db/towers'

	events:
		'mouseover .item' : 'showDescription'
		'mouseout .item'  : 'hideDescription' 
		'click .item'     : 'selectTower'
	
	showDescription: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		$desc = $ "#description-#{$currentTarget.data('id')}"
		$desc.removeClass 'hidden'

	hideDescription: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		$desc = $ "#description-#{$currentTarget.data('id')}"
		$desc.addClass 'hidden'
	
	selectTower: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		towerId = $currentTarget.data 'id'
		events.trigger 'select:tower', towerId

return BuildView