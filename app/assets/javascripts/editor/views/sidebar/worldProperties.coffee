EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
MapProperties = require 'mapProperties'

class WorldProperties extends BasePropertiesView

	id: 'sidebar-properties-world'

	className: 'sidebar-panel'

	entity: 'world'

	template: templates.get 'sidebar/worldProperties.tmpl'

	events:
		'change input[name="name"]'						: 'nameChanged'
		'change textarea[name="description"]'	: 'onChangedString'
		'change select[name="skybox"]'				: 'onChangedString'
		'change select[name="teamSize"]'			: 'onChangedInteger'
		'change input[name="prepareTime"]'		: 'onChangedInteger'
		'change input[name="lives"]'					: 'onChangedInteger'
		'change input[name="goldStart"]'			: 'onChangedInteger'
		'change input[name="goldPerTick"]'		: 'onChangedInteger'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @showPanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @hidePanel
		EditorEventbus.showSidebarPathing.add @hidePanel
		
		
		
	nameChanged: (event) ->
		@onChangedString(event)
		MapProperties.MAP_TITLE = @model.get 'name' 

return WorldProperties