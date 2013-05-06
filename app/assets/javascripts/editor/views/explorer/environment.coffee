EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class EnvironmentExplorer extends BaseView

	entity: 'environmentsStatic'

	template: templates.get 'explorer/item.tmpl'

	events:
		'click .scenegraph-tree-object' : 'selectElement'

	initialize: (options) ->
		@sidebar = db.get 'ui/sidebar'
		@addContextMenu()				
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model
		EditorEventbus.selectObjectViewport.add @selectObject

	selectElement: (event) ->
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		value = $currentTarget.data 'value'
		$('.scenegraph-tree div').removeClass 'active'
		$currentTarget.addClass 'active'
		EditorEventbus.dispatch 'selectObjectUI', value
		@sidebar.set 'active', 'sidebar-properties-object'
		
	removeElement: (key, opt) =>
		id = opt.$trigger.data 'value'
		obj = @model.get id
		EditorEventbus.dispatch 'removeStaticObject', obj

	selectObject: (id) =>
		$('.scenegraph-tree div').removeClass 'active'
		@$("div[data-value='#{id}']").addClass 'active'
		@sidebar.set 'active', 'sidebar-properties-object'

	addContextMenu: ->
		jQuery.contextMenu
			selector			: '.scenegraph-tree-object'
			items					:
				remove			:
					name			: 'Delete'
					callback	: @removeElement

return EnvironmentExplorer