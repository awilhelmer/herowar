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
		'change input[name*="mp-color-"]'	: 'changeColor'
		'change input[name*="mp-basis-"]'	: 'changeBasis'
	
	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @showPanel
		EditorEventbus.selectMaterial.add @loadMaterial
		EditorEventbus.deselectMaterial.add @hidePanel

	loadMaterial: (id) =>
		console.log 'Load Material Properties'
		@model = db.get 'materials', id.id
		console.log @model
		@render()

	changeBasis: (event) =>
		unless event then return
		event.preventDefault()
		entry = @getEntry event, "mp-basis-"
		@model.set entry.property, entry.value
		EditorEventbus.changeMaterial.dispatch @model.id
		
	changeColor: (event) =>
		unless event then return
		event.preventDefault()
		entry = @getEntry event, "mp-color-"
		@model.set entry.property, entry.value
		EditorEventbus.changeMaterial.dispatch @model.id
		
	getTemplateData: ->
		json = super()
		textures = []
		textures.push texture.id for texture in db.get('textures').models
		json.textures = textures
		json

return MaterialProperties