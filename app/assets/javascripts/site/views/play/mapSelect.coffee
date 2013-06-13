BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MapSelectView extends BaseView
	
	className: 'map-select'
		
	entity: 'db/maps'
		
	template: templates.get 'play/mapSelect.tmpl'
	
	events:
		'click li' : 'requestSoloMatch'
	
	initialize: (options) ->
		@matchOpen = db.get 'api/matchOpen'
		@matchMaker = db.get 'api/matchMaker'
		@match = db.get 'api/match'
		super options

	bindEvents: ->
		@listenTo @matchOpen, 'change:token', @render
		@listenTo @matchMaker, 'change:id', @render
		@listenTo @match, 'change:id', @joinGame
		@listenTo @match, 'change:token', @redirect
		super()
		
	getTemplateData: ->
		json = super()
		json.matchOpen = @matchOpen.has 'token'
		json.matchMaker = @matchMaker.has 'id'
		return json
		
	requestSoloMatch: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		id = $currentTarget.data 'id'
		unless id then return
		@match.clear()
		@match.set 'mapId', id
		@match.fetch()

	joinGame: ->
		@match.fetch()
	
	redirect: ->
		window.location = "/game?token=#{@match.get('token')}"
	
return MapSelectView