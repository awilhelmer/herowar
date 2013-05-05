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
		'shown a[data-toggle="tab"]'			: 'changeTab'
		'change input[name*="mp-basis-"]'	: 'changeBasis'
		'change input[name*="mp-color-"]'	: 'changeColor'
	
	initialize: (options) ->
		@tabs = db.get 'ui/tabs'
		@tabs.set 'materialProperties', '#mp-basis'
		super options
	
	bindEvents: ->
		EditorEventbus.selectMaterial.add @loadMaterial

	loadMaterial: (id) =>
		log.debug 'Load Material Properties'
		@model = db.get 'materials', id.id
		@render()

	changeTab: (event) ->
		unless event then return
		$currentTarget = $ event.target
		@tabs.set 'materialProperties', $currentTarget.data 'target'

	changeBasis: (event) ->
		unless event then return
		event.preventDefault()
		entry = @getEntry event, "mp-basis-"
		@model.set entry.property, entry.value
		EditorEventbus.dispatch 'changeMaterial', @model.id
		
	changeColor: (event) ->
		unless event then return
		event.preventDefault()
		entry = @getEntry event, "mp-color-"
		@model.set entry.property, entry.value
		EditorEventbus.dispatch 'changeMaterial', @model.id
		
	getTemplateData: ->
		json = super()
		json.textures = @getTexturesFromDB()
		json.isActiveTabBasis = @tabs.get('materialProperties') is '#mp-basis'
		json.isActiveTabColor = @tabs.get('materialProperties') is '#mp-color'
		json.isActiveTabTexture = @tabs.get('materialProperties') is '#mp-texture'
		json
	
	getTexturesFromDB: ->
		textures = []
		textures.push texture.id for texture in db.get('textures').models
		textures		

return MaterialProperties