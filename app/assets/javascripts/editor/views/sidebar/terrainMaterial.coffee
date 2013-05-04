EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class TerrainMaterial extends BaseView
		
	className: 'mm-material'
	
	entity: 'materials'
	
	template: templates.get 'sidebar/terrainMaterial.tmpl'
	
	events:
		'click' : 'loadMaterial'

	initialize: (options) ->
		@terrain = db.get 'ui/terrain'
		super options

	loadMaterial: (event) =>
		unless event then return
		event.preventDefault()
		@terrain.set 'brushMaterialId', @options.modelId
		EditorEventbus.dispatch 'showMaterialProperties'
		
return TerrainMaterial