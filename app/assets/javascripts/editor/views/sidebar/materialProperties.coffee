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
		EditorEventbus.selectMaterial.add @loadMaterial

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