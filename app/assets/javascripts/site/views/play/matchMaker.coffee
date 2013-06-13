BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchMakerView extends BaseView
	
	className: 'match-maker'
				
	template: templates.get 'play/matchMaker.tmpl'
	
	entity: 'api/match'
	
	events:
		'click .create' : 'gameCreate'
		'click .join' : 'gameJoin'

	gameCreate: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		id = $currentTarget.data 'id'
		unless id then return
		@model.clear()
		@model.set 'mapId', id
		@model.fetch()
		
	gameJoin: (event) ->
	
return MatchMakerView