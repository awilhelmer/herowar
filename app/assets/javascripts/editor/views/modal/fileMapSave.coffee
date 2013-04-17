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
			contentType	: 'application/json; charset=utf-8'
			data				: JSON.stringify @getMapAsJSON()
			success			: (data, textStatus, jqXHR) =>
				console.log 'Save map SUCCESS'
				@status.isSuccessful = true
			error				: (jqXHR, textStatus, errorThrown) =>
				console.log 'Save map ERROR'
				@status.isError = true
		jqxhr.done =>
			@status.isSaving = false		

	getMapAsJSON: ->
		id									: MapProperties.MAP_ID
		name 								: MapProperties.MAP_TITLE
		description 				: MapProperties.MAP_DESCRIPTION
		teamSize 						: MapProperties.MAP_TEAM_SIZE
		prepareTime					: MapProperties.MAP_PREPARE_TIME
		lives 							: MapProperties.MAP_LIVES
		goldStart 					: MapProperties.MAP_GOLD_START
		goldPerTick 				: MapProperties.MAP_GOLD_PER_TICK
		terrain							:
			id								: MapProperties.TERRAIN_ID
			width							: MapProperties.TERRAIN_WIDTH
			height						: MapProperties.TERRAIN_HEIGHT
			smoothness				: MapProperties.TERRAIN_SMOOTHNESS
			zScale						: MapProperties.TERRAIN_ZSCALE
			geometry					:
				id							: MapProperties.GEOMETRY_ID
				faces						: "#{JSON.stringify(MapProperties.TERRAIN_FACES)}"
				vertices				: "#{JSON.stringify(MapProperties.TERRAIN_VERTICES)}"
				materials				: "#{JSON.stringify(MapProperties.TERRAIN_MATERIALS)}"
				metadata				:
					id						: MapProperties.GEOMETRY_METADATA_ID
					formatVersion	: 3.1
					sourceFile		: ''
					generatedBy		: 'MapEditor'
					vertices			: MapProperties.TERRAIN_VERTICES.length
					faces					: MapProperties.TERRAIN_FACES.length
					normal				: 0
					colors				: 0
					usvs					: 0
					materials			: MapProperties.TERRAIN_MATERIALS.length
				
return ModalFileMapSave