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
		@listenTo @model, 'change:id', @joinGame

	gameCreate: (event) ->
		@model.clear()
		@model.set 'mapId', 102 # TODO: map id should be dynamic ... but we have just one map right now ;)
		console.log 'Create match for', @model
		@model.fetch()

	joinGame: ->
		if @model.has 'id'
			@matchToken = db.get 'api/matchToken'
			@matchToken.set 'id', @model.get 'id'
			@matchToken.fetch()
		@render()
		
return MatchCreateView