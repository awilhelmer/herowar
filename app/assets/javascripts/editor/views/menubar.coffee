MapProperties = require 'mapProperties'
BaseView = require 'views/baseView'
templates = require 'templates'

class Menubar extends BaseView

	id: 'menubar'
	
	className: 'navbar navbar-fixed-top'
	
	template: templates.get 'menubar.tmpl'
	
	events:
		'click #fileNewMapEmpty'			: 'fileNewMapEmpty'
		'click #fileNewMapGenerated'	: 'fileNewMapGenerated'
		'click #fileOpen'							: 'fileOpen'	
		'click #fileSave'							: 'fileSave'	
		'click #fileExit'							: 'fileExit'	

	fileNewMapEmpty: (event) ->
		event?.preventDefault()
		$('#modelFileNewMapEmpty').modal 'show'

	fileNewMapGenerated: (event) ->
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileOpen: (event) ->
		event?.preventDefault()
		alert 'Not implemented yet...'

	fileSave: (event) ->
		event?.preventDefault()
		exportObj =
			id						: 0
			name 					: 'Tutorial'
			description 	: ''
			teamSize 			: 1
			prepareTime		: 500
			lives 				: 20
			goldStart 		: 2000
			goldPerTick 	: 5
			terrain				:
				id					: 0
				width				: 500
				height			: 500
				smoothness	: 0.1
				zScale			: 0
		console.log @getMapAsJSON()
		alert 'Not implemented yet...'

	fileExit: (event) ->
		event?.preventDefault()
		$('#modelFileExit').modal 'show'

	getMapAsJSON: ->
		exportObj =
			id						: MapProperties.MAP_ID
			name 					: MapProperties.MAP_TITLE
			description 	: MapProperties.MAP_DESCRIPTION
			teamSize 			: MapProperties.MAP_TEAM_SIZE
			prepareTime		: MapProperties.MAP_PREPARE_TIME
			lives 				: MapProperties.MAP_LIVES
			goldStart 		: MapProperties.MAP_GOLD_START
			goldPerTick 	: MapProperties.MAP_GOLD_PER_TICK
			terrain				:
				id					: MapProperties.TERRAIN_ID
				width				: MapProperties.TERRAIN_WIDTH
				height			: MapProperties.TERRAIN_HEIGHT
				smoothness	: MapProperties.TERRAIN_SMOOTHNESS
				zScale			: MapProperties.TERRAIN_ZSCALE
		JSON.stringify exportObj

return Menubar