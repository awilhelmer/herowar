BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'
db = require 'database'

class MatchMakerView extends BaseView
	
	className: 'match-maker'
				
	template: templates.get 'play/matchMaker.tmpl'
	
	entity: 'api/matchMaker'

	events:
		'click .start' : 'matchStart'
		'click .quit' : 'matchQuit'
	
	bindEvents: ->
		@listenTo @model, 'change:id', @updateMatch
		super()
			
	updateMatch: =>
		if @model.has 'id'
			console.log 'Update match id=', @model.get('id')
			@xhr = @model.fetch merge: true
			setTimeout @updateMatch, 2000
		return

	matchQuit: ->
		@xhr?.abort()
		@model.clear()
		$.ajax
			url: "#{app.resourcePath()}game/match/quit"
		console.log 'Quit match', @model.attributes
		return
		
	matchStart: ->
		console.log 'Start match'
		matchToken = db.get 'api/matchToken'
		window.location = "/game?token=#{matchToken.get('token')}" if matchToken.has 'token'
		return

return MatchMakerView