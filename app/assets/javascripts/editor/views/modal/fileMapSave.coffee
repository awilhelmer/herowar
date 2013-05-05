BaseModalView = require 'views/baseModalView'
templates = require 'templates'
log = require 'util/logger'

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
		db = require 'database'
		world = db.get 'world'
		errors = []
		errors.push 'Map title is required' unless world.get 'name'
		errors
		
	saveMap: ->
		@status.isSaving = true
		json = @getMapAsJson()
		log.info 'Save map'
		jqxhr = $.ajax
			url					: '/api/editor/map'
			type				: 'POST'
			dataType		: 'json'
			contentType	: 'application/json; charset=utf-8'
			data				: json
			success			: @onSuccess
			error				: @onError
		jqxhr.done @onDone

	getMapAsJson: ->
		worldToObjectConverter = require 'util/worldToObjectConverter'
		obj = worldToObjectConverter.convert()
		#console.log 'Prepared map object for saving:'
		#console.log obj
		JSON.stringify obj

#Data is saved Map Id
	onSuccess: (data, textStatus, jqXHR) =>
		console.log 'Save map SUCCESS'
		window.location.href = "/editor?map=" + data
		@status.isSuccessful = true

	onError: (jqXHR, textStatus, errorThrown) =>
		log.error 'Save map ERROR'
		@status.isError = true

	onDone: =>
		log.info 'Saved map successfully'
		@status.isSaving = false	
		

return ModalFileMapSave