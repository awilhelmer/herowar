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
				position = @model.get 'position'
				position.x = parseFloat $currentTarget.val()
				@model.set 'positon', position
			when 'positionY'
				position = @model.get 'position'
				position.y = parseFloat $currentTarget.val()
				@model.set 'positon', position
			when 'positionZ'
				position = @model.get 'position'
				position.z = parseFloat $currentTarget.val()
				@model.set 'positon', position
			when 'scaleX'
				scale = @model.get 'scale'
				scale.x = parseFloat $currentTarget.val()
				@model.set 'scale', scale
			when 'scaleY'
				scale = @model.get 'scale'
				scale.y = parseFloat $currentTarget.val()
				@model.set 'positon', scale
			when 'scaleZ'
				scale = @model.get 'scale'
				scale.z = parseFloat $currentTarget.val()
				@model.set 'scale', scale
			when 'rotationX'
				rotation = @model.get 'rotation'
				rotation.x = parseFloat $currentTarget.val()
				@model.set 'rotation', rotation
			when 'rotationY'
				rotation = @model.get 'rotation'
				rotation.y = parseFloat $currentTarget.val()
				@model.set 'rotation', rotation
			when 'rotationZ'
				rotation = @model.get 'rotation'
				rotation.z = parseFloat $currentTarget.val()
				@model.set 'rotation', rotation
		EditorEventbus.changeStaticObject.dispatch @model

return ObjectProperties