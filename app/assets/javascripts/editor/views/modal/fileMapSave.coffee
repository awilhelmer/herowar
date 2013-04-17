BaseModalView = require 'views/baseModalView'
MapProperties = require 'mapProperties'
templates = require 'templates'

class ModalFileMapSave extends BaseModalView

	id: 'modalFileMapSave'
	
	className: 'modal hide fade'
		
	template: templates.get 'modal/fileMapSave.tmpl'

	initialize: (options) ->
		@errors = @getErrors()
		super options

	onShow: (event) =>
		@errors = @getErrors()
		@status = 
			isSaving 			: false
			isSuccessful 	: false
			isError				: false
		@saveMap() if @errors.length is 0
		@render()
		super event
		
	onShown: (event) =>
		setTimeout @hide, 1000 if @errors.length is 0
		super event

	getTemplateData: ->
		errors: @errors
		isValid: @errors.length is 0
		status: @status

	getErrors: ->
		errors = []
		errors.push 'Map title is required' unless MapProperties.MAP_TITLE
		errors

	saveMap: ->
		@status.isSaving = true
		jqxhr = $.ajax
			url					: '/api/editor/map'
			type				: 'POST'
			dataType		: 'json'
			data				:
				map				: @getMapAsJSON()
			success			: (data, textStatus, jqXHR) =>
				console.log 'Save map SUCCESS'
				@status.isSuccessful = true
			error				: (jqXHR, textStatus, errorThrown) =>
				console.log 'Save map ERROR'
				@status.isError = true
		jqxhr.done =>
			@status.isSaving = false		

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

return ModalFileMapSave