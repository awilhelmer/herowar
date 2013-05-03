EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class EnvironmentExplorer extends BaseView

	entity: 'environmentsStatic'

	template: templates.get 'sidebar/explorer/environment.tmpl'

	events:
		'click .scenegraph-tree-object' : 'selectElement'

	initialize: (options) ->
		@addContextMenu()				
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	selectElement: (event) ->
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		value = $currentTarget.data 'value'
		$('.scenegraph-tree div').removeClass 'active'
		$currentTarget.addClass 'active'
		EditorEventbus.selectObjectUI.dispatch value
		EditorEventbus.showObjectProperties.dispatch()
		
	removeElement: (key, opt) =>
		id = opt.$trigger.data 'value'
		obj = @model.get id
		@model.remove obj
		EditorEventbus.removeStaticObject.dispatch obj

	addContextMenu: ->
		jQuery.contextMenu
			selector			: '.scenegraph-tree-object'
			items					:
				remove			:
					name			: 'Delete'
					callback	: @removeElement

return EnvironmentExplorer