BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchJoinView extends BaseView
	
	className: 'match-join'
				
	template: templates.get 'play/matchJoin.tmpl'
	
	entity: 'api/matchJoin'
	
	events:
		'click' : 'gameJoin'
		
	bindEvents: ->
		@listenTo @model, 'change:id', @joinGame

	gameJoin: (event) ->
		@model.clear()
		@model.fetch()

	joinGame: ->
		if @model.has 'id'
			@matchToken = db.get 'api/matchToken'
			@matchToken.set 'id', @model.get 'id'
			@matchToken.fetch()
		@render()

return MatchJoinView