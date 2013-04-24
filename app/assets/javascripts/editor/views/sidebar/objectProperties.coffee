EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
db = require 'database'

class ObjectProperties extends BasePropertiesView
	
	id: 'sidebar-properties-object'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/objectProperties.tmpl'
	
	events:
		'change input'	: 'changeInput'
	
	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @showPanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @hidePanel
		EditorEventbus.showSidebarPathing.add @hidePanel
		EditorEventbus.selectObjectUI.add @selectItem

	selectItem: (value) =>
		@model = db.get 'environmentsStatic', value
		@render()

	changeInput: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		name = $currentTarget.attr 'name'
		switch name
			when 'positionX'
				@model.get('position').x = parseFloat $currentTarget.val()
				@triggerChange 'position'
			when 'positionY'
				@model.get('position').y = parseFloat $currentTarget.val()
				@triggerChange 'position'
			when 'positionZ'
				@model.get('position').z = parseFloat $currentTarget.val()
				@triggerChange 'position'
			when 'scaleX'
				@model.get('scale').x = parseFloat $currentTarget.val()
				@triggerChange 'scale'
			when 'scaleY'
				@model.get('scale').y = parseFloat $currentTarget.val()
				@triggerChange 'scale'
			when 'scaleZ'
				@model.get('scale').z = parseFloat $currentTarget.val()
				@triggerChange 'scale'
			when 'rotationX'
				@model.get('rotation').x = parseFloat $currentTarget.val()
				@triggerChange 'rotation'
			when 'rotationY'
				@model.get('rotation').y = parseFloat $currentTarget.val()
				@triggerChange 'rotation'
			when 'rotationZ'
				@model.get('rotation').z = parseFloat $currentTarget.val()
				@triggerChange 'rotation'
		EditorEventbus.changeStaticObject.dispatch @model

	triggerChange: (propertyName) ->
		@model.trigger "change:#{propertyName}"
		@model.trigger 'change'

return ObjectProperties