EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class TerrainMaterial extends BaseView
		
	className: 'mm-material'
	
	entity: 'materials'
	
	template: templates.get 'sidebar/terrainMaterial.tmpl'
	
	events:
		'click' : 'loadMaterial'
	
	loadMaterial: (event) =>
		unless event then return
		event.preventDefault()
		@dispatchSelectMaterialEvent @options.modelId

	dispatchSelectMaterialEvent: (modelId) ->
		EditorEventbus.menuSelectMaterial.dispatch modelId
		EditorEventbus.showMaterialProperties.dispatch()

return TerrainMaterial