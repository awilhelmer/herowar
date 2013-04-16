BaseModalView = require 'views/baseModalView'
MapProperties = require 'mapProperties'
templates = require 'templates'

class ModalFileMapSave extends BaseModalView

	id: 'modalFileMapSave'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapSave.tmpl'

	events:
		'click .btn-primary' : 'mapSave'

	mapSave: (event) =>
		unless event then return
		console.log @getMapAsJSON()

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
				faces				: MapProperties.TERRAIN_FACES
				vertices		: MapProperties.TERRAIN_VERTICES
				materials		: MapProperties.TERRAIN_MATERIALS
		JSON.stringify exportObj

	onShow: =>
		@render()

	getTemplateData: ->
		err = @getErrors()
		errors: err
		isValid: err.length is 0

	getErrors: ->
		errors = []
		errors.push 'Map title is required' unless MapProperties.MAP_TITLE
		errors

return ModalFileMapSave