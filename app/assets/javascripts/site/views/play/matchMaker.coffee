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
		@listenTo @model, 'change:state', @joinMatch
		super()
			
	updateMatch: =>
		if @model.has 'id'
			@xhr = @model.fetch merge: true
			setTimeout @updateMatch, 2000
		return

	matchQuit: ->
		@xhr?.abort()
		@model.clear()
		$.ajax
			url: "#{app.resourcePath()}game/match/quit"
		return
		
	matchStart: ->
		console.log 'Start match'
		@$('.start').addClass 'disabled'
		@$('.quit').addClass 'disabled'
		$.ajax
			url: "#{app.resourcePath()}game/match/start/#{@model.get('id')}"		
		return

	joinMatch: ->
		if @model.has('state') and (@model.get('state') is 'PRELOAD' or @model.get('state') is 'GAME')
			matchToken = db.get 'api/matchToken'
			window.location = "/game?token=#{matchToken.get('token')}" if matchToken.has 'token'
		return

return MatchMakerView