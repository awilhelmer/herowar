EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
db = require 'database'

class MaterialProperties extends BasePropertiesView
	
	id: 'sidebar-properties-material'
	
	className: 'sidebar-panel hidden'
	
	entity: 'material'
	
	template: templates.get 'sidebar/materialProperties.tmpl'
	
	events:
		'change input[name="mp-basis-name"]': 'changeName'
		'change input[name="mp-color-sel"]'	: 'changeColor'
	
	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @showPanel
		EditorEventbus.selectMaterial.add @loadMaterial
		EditorEventbus.deselectMaterial.add @hidePanel

	loadMaterial: (materialId) =>
		console.log 'Load Material Properties'
		@model = db.get 'materials', materialId
		console.log @model
		@render()

	changeName: (event) =>
		if event
			event.preventDefault()
			$currentTarget = $ event.currentTarget
			console.log "Name: #{$currentTarget.val()}"
			@model.set 'name', $currentTarget.val()		

	changeColor: (event) =>
		if event
			event.preventDefault()
			$currentTarget = $ event.currentTarget
			console.log "Color: #{$currentTarget.val()}"
			@model.set 'color', $currentTarget.val()

return MaterialProperties