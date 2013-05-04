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

	bindEvents: ->
		EditorEventbus.changeMaterial.add @changeMaterial
		EditorEventbus.menuSelectMaterial.add @selectMaterial
		

	initialize: (options) ->
		col = db.get 'materials'
		if col.models.length > 0
			@nextId = col.models.length
		else
			@nextId = 1
		@nextMatId = -1
		log.debug "Set next material id to #{@nextId}"
		super options
	
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
		EditorEventbus.selectMaterial.dispatch id: id, materialId: matId
		

	selectMaterial: (id) =>
		matId =  @getMaterialId id
		log.debug "Select material id #{id} matId #{matId}"
		idMapper = id: id, materialId:matId
		Constants.MATERIAL_SELECTED = id
		EditorEventbus.selectMaterial.dispatch idMapper

	changeMaterial: (id) =>
		matId =  @getMaterialId id
		log.debug "Changed material id #{id} matId #{matId}"
		idMapper = id: id, materialId: matId
		EditorEventbus.selectMaterial.dispatch idMapper
		EditorEventbus.updateModelMaterial.dispatch idMapper

	getMaterialId: (id) ->
		material = db.get 'materials', id
		material.attributes.materialId
	
	getId: (materialId) ->
		material = db.find 'materials', materialId: materialId
		material.attributes.id

return ScenebarTerrainView