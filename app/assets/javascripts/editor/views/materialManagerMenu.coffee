EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
Material = require 'models/material'
db = require 'database'
Constants = require 'constants'

class MaterialManagerMenu extends BaseView

	id: 'materialManagerMenu'

	template: templates.get 'materialManagerMenu.tmpl'

	events:
		'click .mm-new-material' : 'newMaterial'
	
	bindEvents: ->
		EditorEventbus.changeMaterial.add @changeMaterial
		EditorEventbus.menuSelectMaterial.add @selectMaterial
		
	
	#TODO on preloading fill all materials from - initialize from preloader
	initialize: (options) ->
		@nextId = 1
		@nextMatId = 1 #TODO get next id from database! (Create new empty Material ... )
		@idmapper = [id:@nextId, materialId:@nextMatId]
		super options
		
	updateMatId: (id, materialId) ->
		for entry in @idmapper
			unless found	
				for key, value of entry
					if key is id
						value = materialId
						found = true
						break
			else 
				break
		unless found
			@idmapper.push id: id, materialId: materialId
					
	getMaterialId: (id) ->
		for entry in @idmapper
			if entry.id is id
				result = entry.materialId
		result
	
	getId: (materialId) ->
		for entry in @idmapper
			if entry.materialId is materialId
				result = entry.id
		result

	selectMaterial: (id) =>
		matId =  @getMaterialId id
		modelId = id
		console.log "Select material id #{modelId} matId #{matId}"
		idMapper = id: modelId, materialId:matId
		Constants.MATERIAL_SELECTED = id
		EditorEventbus.selectMaterial.dispatch idMapper

	changeMaterial: (id) =>
		matId =  @getMaterialId id
		console.log "Changed material id #{id} matId #{matId}"
		idMapper = id: id, materialId: matId
		EditorEventbus.selectMaterial.dispatch idMapper
		EditorEventbus.updateModelMaterial.dispatch idMapper
		
	newMaterial: (event) =>
		event?.preventDefault()
		id = ++@nextId
		matId = ++@nextMatId #TODO get next id from database! (Create new empty Material ... )
		console.log "New material id #{id} matId #{matId}"
		col = db.get 'materials'
		mat = new Material()
		mat.set 
			'id'					: id
			'materialId' 	: matId
			'name' 				: "Mat.#{id}"
			'color' 			: '#CCCCCC'
			'transparent' : false
			'opacity'			: 1
			'map'					: undefined		
		col.add mat
		@updateMatId id, matId
		EditorEventbus.selectMaterial.dispatch id: id, materialId: matId
		
return MaterialManagerMenu