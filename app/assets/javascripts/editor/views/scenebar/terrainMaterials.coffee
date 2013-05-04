EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
Constants = require 'constants'
templates = require 'templates'
db = require 'database'

class ScenebarTerrainMaterials extends BaseView

	entity: 'materials'

	template: templates.get 'scenebar/terrainMaterials.tmpl'

	events:
		'click .mm-material' : 'loadMaterial'
	
	initialize: (options) ->
		@sidebar = db.get 'ui/sidebar'
		@terrain = db.get 'ui/terrain'
		super options
	
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render
		@listenTo @terrain, 'change:brushMaterialId', @render
	
	loadMaterial: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		$('#scenebar-terrain-materials .mm-material').removeClass 'active'
		$currentTarget.addClass 'active'
		modelId = $currentTarget.data 'matid'
		if modelId
			@terrain.set 'brushMaterialId', modelId
			@sidebar.set 'active', 'sidebar-properties-material'

	render: ->
		super()
		$selectedMaterial = @$ "div[data-matid='#{@terrain.get('brushMaterialId')}']"
		$selectedMaterial.addClass 'active' if $selectedMaterial.length isnt 0

return ScenebarTerrainMaterials