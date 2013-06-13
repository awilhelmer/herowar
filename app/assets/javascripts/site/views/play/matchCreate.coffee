BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchCreateView extends BaseView
	
	className: 'match-create'
				
	template: templates.get 'play/matchCreate.tmpl'
	
	entity: 'api/matchCreate'
	
	events:
		'click' : 'gameCreate'
		
	bindEvents: ->
		@listenTo @model, 'change:id', @joinOwnGame
		@listenTo @model, 'change:udate', @render

	gameCreate: (event) ->
		@model.clear()
		@model.set 'mapId', 102 # TODO: map id should be dynamic ... but we have just one map right now ;)
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
		
return MatchCreateView