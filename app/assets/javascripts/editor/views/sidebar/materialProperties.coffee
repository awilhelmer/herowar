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
		'change input[name="mp-basis-name"]'					: 'changeName'
		'change input[name="mp-color-sel"]'						: 'changeColor'
		'change input[name="mp-basis-transparent"]' 	: 'changeTransparency'
		'change input[name="mp-basis-opacity"]' 			: 'changeOpacity'
	
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
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		console.log "Name: #{$currentTarget.val()}"
		@model.set 'name', $currentTarget.val()		

	changeColor: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		console.log "Color: #{$currentTarget.val()}"
		@model.set 'color', $currentTarget.val()
		EditorEventbus.changeMaterial.dispatch @model
		
	changeTransparency: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		console.log "Transparency: #{$currentTarget.val()}"
		@model.set 'transparent', $currentTarget.val()
		EditorEventbus.changeMaterial.dispatch @model
	
	changeOpacity: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		if (@model.get 'opacity') isnt $currentTarget.val()
			console.log "Opacity: #{$currentTarget.val()}"
			@model.set 'opacity', $currentTarget.val()
			EditorEventbus.changeMaterial.dispatch @model

	getTemplateData: ->
		json = super()
		textures = []
		textures.push texture.id for texture in db.get('textures').models
		json.textures = textures
		json

return MaterialProperties