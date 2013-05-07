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