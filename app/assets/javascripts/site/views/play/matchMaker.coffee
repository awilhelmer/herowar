BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchMakerView extends BaseView
	
	className: 'match-maker'
				
	template: templates.get 'play/matchMaker.tmpl'
	
	entity: 'api/matchMaker'
	
	events:
		'click .create' : 'gameCreate'
		'click .join' : 'gameJoin'
		
	bindEvents: ->
		@listenTo @model, 'change:id', @joinOwnGame
		@listenTo @model, 'change:udate', @render

	gameCreate: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		id = $currentTarget.data 'id'
		unless id then return
		@model.clear()
		@model.set 'mapId', id
		@model.fetch()

	joinOwnGame: ->
		@matchToken = db.get 'api/matchToken'
		@matchToken.set 'id', @model.get 'id'
		@matchToken.fetch()
		@render()
		setTimeout @checkOwnGame, 1000
	
	checkOwnGame: =>
		@model.fetch()
		setTimeout @checkOwnGame, 1000
	
	gameJoin: (event) ->
	
return MatchMakerView