BaseView = require 'views/baseView'
templates = require 'templates'
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
			@model.fetch()
			setTimeout @updateMatch, 2000
		return

	matchQuit: ->
		@model.quit()
		@model.clear()
		console.log 'Quit match', @model.attributes
		return
		
	matchStart: ->
		console.log 'Start match'
		matchToken = db.get 'api/matchToken'
		window.location = "/game?token=#{matchToken.get('token')}" if matchToken.has 'token'
		return

return MatchMakerView