EditorEventbus = require 'editorEventbus'
Material = require 'models/material'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class ScenebarTerrainView extends BaseView
	
	template: templates.get 'scenebar/terrain.tmpl'

	events:
		'click #scenebar-terrain-material-add' : 'newMaterial'
	
	#TODO on preloading fill all materials from - initialize from preloader
	initialize: (options) ->
		@nextId = 1
		@nextMatId = 1 #TODO get next id from database! (Create new empty Material ... )
		@idmapper = [id:@nextId, materialId:@nextMatId]
		super options

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

return ScenebarTerrainView