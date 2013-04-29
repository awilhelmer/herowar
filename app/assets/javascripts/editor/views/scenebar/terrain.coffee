EditorEventbus = require 'editorEventbus'
Material = require 'models/material'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'
Constants = require 'constants'

class ScenebarTerrainView extends BaseView
	
	template: templates.get 'scenebar/terrain.tmpl'

	events:
		'click #scenebar-terrain-material-add' : 'newMaterial'

	bindEvents: ->
		EditorEventbus.changeMaterial.add @changeMaterial
		EditorEventbus.menuSelectMaterial.add @selectMaterial
		

	initialize: (options) ->
		@nextId = 1
		@nextMatId = -1
		@idmapper = [id:@nextId, materialId:@nextMatId]
		super options

	newMaterial: (event) =>
		event?.preventDefault()
		id = ++@nextId
		matId = --@nextMatId 
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

	selectMaterial: (id) =>
		matId =  @getMaterialId id
		console.log "Select material id #{id} matId #{matId}"
		idMapper = id: id, materialId:matId
		Constants.MATERIAL_SELECTED = id
		EditorEventbus.selectMaterial.dispatch idMapper

	changeMaterial: (id) =>
		matId =  @getMaterialId id
		console.log "Changed material id #{id} matId #{matId}"
		idMapper = id: id, materialId: matId
		EditorEventbus.selectMaterial.dispatch idMapper
		EditorEventbus.updateModelMaterial.dispatch idMapper
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

return ScenebarTerrainView