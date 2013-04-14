EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
Material = require 'models/material'
db = require 'database'

class MaterialManagerMenu extends BaseView

	id: 'materialManagerMenu'

	template: templates.get 'materialManagerMenu.tmpl'

	events:
		'click .mm-new-material' : 'newMaterial'
	
	bindEvents: ->
		EditorEventbus.changeMaterial.add @changeMaterial
	
	initialize: (options) ->
		@nextId = 2
		@nextMatId = 2
		@idmapper = []
		super options
	
	updateMatId: (id, materialId) ->
		for entry in @idmapper
			unless found	
				for key,value of entry
					if key is id
						value = materialId
						found = true
						break
			else 
				break
		unless found
			@idmapper.push id: id, materialId:materialId
					
	getMaterialId: (id) ->
		for entry in @idmapper
			if entry.id is id
				result = entry.materialId
		materialId
		
	changeMaterial: (material) =>
		matId = @nextMatId++
		material.set 
			'materialId'	: matId
		id = material.get 'id'
		@updateMatId id, matId
		EditorEventbus.selectMaterial.dispatch id, matId 
		
	newMaterial: (event) =>
		event?.preventDefault()
		col = db.get 'materials'
		mat = new Material()
		id = @nextId++
		matId = @nextMatId++
		mat.set 
			'id'					: id
			'materialId' 	: matId
			'name'				: "Mat.#{id}"
			'color' 			: '#CCCCCC'
			'transparent' : false
			'opacity'			: 1
			'map'					: undefined
			
		col.add mat
		updateMatId id matId
		
return MaterialManagerMenu