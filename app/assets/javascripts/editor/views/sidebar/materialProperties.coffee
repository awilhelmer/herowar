EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
log = require 'util/logger'
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
		EditorEventbus.showSidebarEnvironment.add @hidePanel
		EditorEventbus.showSidebarPathing.add @hidePanel
		EditorEventbus.showPathingProperties.add @hidePanel
		EditorEventbus.selectMaterial.add @loadMaterial
		EditorEventbus.deselectMaterial.add @hidePanel

	loadMaterial: (id) =>
		log.debug 'Load Material Properties'
		@model = db.get 'materials', id.id
		@render()

	changeBasis: (event) =>
		unless event then return
		event.preventDefault()
		entry = @getEntry event, "mp-basis-"
		@model.set entry.property, entry.value
		EditorEventbus.dispatch 'changeMaterial', @model.id
		
	changeColor: (event) =>
		unless event then return
		event.preventDefault()
		entry = @getEntry event, "mp-color-"
		@model.set entry.property, entry.value
		EditorEventbus.dispatch 'changeMaterial', @model.id
		
	getTemplateData: ->
		json = super()
		textures = []
		textures.push texture.id for texture in db.get('textures').models
		json.textures = textures
		json

return MaterialProperties