BaseModalView = require 'views/baseModalView'
templates = require 'templates'
db = require 'database'
EditorEventbus = require 'editorEventbus'

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
		world = db.get 'world'
		errors = []
		errors.push 'Map title is required' unless world.get 'name'
		errors

	fillMaterialArray: (data) ->
		materials = []
		for index in data.materials
			materials.push db.get 'materials', index 
		data.materials = materials
		
	saveMap: ->
		@status.isSaving = true
		world = db.get 'world'
		materials = world.attributes.materials
		@handleMaterials world.attributes
		@fillMaterialArray world.attributes
		json = JSON.stringify world.attributes
		#console.log 'json: ' + json
		jqxhr = $.ajax
			url					: '/api/editor/map'
			type				: 'POST'
			dataType		: 'json'
			contentType	: 'application/json; charset=utf-8'
			data				: json
			success			: @onSuccess
			error				: @onError
		jqxhr.done @onDone
		world.attributes.materials = materials


	onSuccess: (data, textStatus, jqXHR) =>
		console.log 'Save map SUCCESS'
		#@parseSuccessResponse data if _.isObject data
		@status.isSuccessful = true

	onError: (jqXHR, textStatus, errorThrown) =>
		console.log 'Save map ERROR'
		@status.isError = true

	onDone: =>
		console.log 'Save map DONE'
		@status.isSaving = false

	
	handleMaterials: ->
		 EditorEventbus.handleWorldMaterials.dispatch()
	
	
	parseSuccessResponse: (data) ->
		# MapProperties.MAP_ID = data.id
		# MapProperties.TERRAIN_ID = data.terrain.id
		# MapProperties.GEOMETRY_ID = data.terrain.geometry.id
		# MapProperties.GEOMETRY_METADATA_ID = data.terrain.geometry.metadata.id

return ModalFileMapSave