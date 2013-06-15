BaseView = require 'views/baseView'
templates = require 'templates'

class Sidebar extends BaseView

	id: 'sidebar'
	
	entity: 'ui/sidebar'
	
	template: templates.get 'sidebar.tmpl'
	
	initialize: (options) ->
		super options
		@model.set 'active', 'sidebar-properties-world'

	getTemplateData: ->
		json = super()
		json.isActivePropertiesWorld = if json.active is 'sidebar-properties-world' then true else false
		json.isActivePropertiesTerrain = if json.active is 'sidebar-properties-terrain' then true else false
		json.isActivePropertiesObject = if json.active is 'sidebar-properties-object' then true else false
		json.isActivePropertiesMaterial = if json.active is 'sidebar-properties-material' then true else false
		json.isActivePropertiesPathing = if json.active is 'sidebar-properties-pathing' then true else false
		json.isActivePropertiesWave = if json.active is 'sidebar-properties-wave' then true else false
		json.isActiveEnvironment = if json.active is 'sidebar-environment' then true else false
		json.isActivePathing = if json.active is 'sidebar-pathing' then true else false
		json.isActiveWaves = if json.active is 'sidebar-waves' then true else false
		json
	
return Sidebar