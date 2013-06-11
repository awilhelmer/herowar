DateFormat = require 'util/dateFormat'
AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'
db = require 'database'

class Play extends AuthView

	id: 'play'
	
	entity: 'db/maps'
	
	template: templates.get 'play.tmpl'

	events:
		'click .map' : 'joinMap'
		
	initialize: (options) ->
		@me = db.get 'ui/me'
		super options

	getTemplateData: ->
		json = super()
		if @me.get 'player'
			json.results = @me.get('player').gameResults
			result.formattedCdate = DateFormat.format new Date(result.cdate), 'ddd mmm dd hh:nn:ss' for result in json.results
		return json

	joinMap: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		id = $currentTarget.data 'id'
		unless id then return
		gameToken = db.get "api/gameToken"
		gameToken.id = id
		gameToken.fetch success: @onSuccess
	
	onSuccess: (response) =>
		window.location = "/game?token=#{response.get('token')}"
	
return Play