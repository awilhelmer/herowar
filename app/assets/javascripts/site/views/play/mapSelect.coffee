BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MapSelectView extends BaseView
	
	className: 'map-select'
		
	entity: 'db/maps'
		
	template: templates.get 'play/mapSelect.tmpl'
	
	events:
		'click li' : 'requestMatch'
	
	initialize: (options) ->
		@match = db.get 'api/match'
		super options

	bindEvents: ->
		@listenTo @match, 'change:id', @joinGame
		@listenTo @match, 'change:token', @redirect
		super()
		
	requestMatch: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		id = $currentTarget.data 'id'
		unless id then return
		@match.set 'mapId', id
		@match.fetch()

	joinGame: ->
		@match.fetch()
	
	redirect: ->
		window.location = "/game?token=#{@match.get('token')}"
	
return MapSelectView