EditorEventbus = require 'editorEventbus'
Material = require 'models/material'
BaseView = require 'views/baseView'
templates = require 'templates'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class ScenebarTerrainView extends BaseView
	
	template: templates.get 'scenebar/terrain.tmpl'

	events:
		'click #scenebar-terrain-material-add' : 'newMaterial'

	initialize: (options) ->
		@terrain = db.get 'ui/terrain'
		col = db.get 'materials'
		if col.models.length > 0
			@nextId = col.models.length
		else
			@nextId = 1
		@nextMatId = -1
		log.debug "Set next material id to #{@nextId}"
		super options
	
	bindEvents: ->
		EditorEventbus.changeMaterial.add @changeMaterial
		@listenTo @terrain, 'change:brushMaterialId', @selectMaterial
	
	newMaterial: (event) =>
		event?.preventDefault()
		id = ++@nextId
		matId = --@nextMatId 
		log.debug "New material id #{id} matId #{matId}"
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
		@terrain.set 'brushMaterialId', id
		EditorEventbus.dispatch 'selectMaterial', id: id, materialId: matId
		

	selectMaterial: (model) =>
		id = model.get 'brushMaterialId'
		matId =  @getMaterialId id
		log.debug "Select material id #{id} matId #{matId}"
		idMapper = id: id, materialId:matId
		Constants.MATERIAL_SELECTED = id
		EditorEventbus.dispatch 'selectMaterial', idMapper

	changeMaterial: (id) =>
		matId =  @getMaterialId id
		log.debug "Changed material id #{id} matId #{matId}"
		idMapper = id: id, materialId: matId
		EditorEventbus.dispatch 'selectMaterial', idMapper
		EditorEventbus.dispatch 'updateModelMaterial', idMapper

	getMaterialId: (id) ->
		material = db.get 'materials', id
		material.attributes.materialId
	
	getId: (materialId) ->
		material = db.find 'materials', materialId: materialId
		material.attributes.id

return ScenebarTerrainView