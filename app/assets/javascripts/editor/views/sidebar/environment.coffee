EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
db = require 'database'

class Environment extends BasePropertiesView

	id: 'sidebar-environment'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/environment.tmpl'

	events:
		'click #sidebar-environment-remove'	: 'removeItem'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @showPanel
		EditorEventbus.showSidebarPathing.add @hidePanel
		EditorEventbus.showPathingProperties.add @hidePanel
		EditorEventbus.listSelectItem.add @listSelectItem

	initialize: (options) ->
		@selectedItem = null
		super options

	listSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-list'
			@selectedItem = db.get 'environmentsStatic', value
			$removeButton = @$ '#sidebar-environment-remove'
			$removeButton.addClass 'show' unless $removeButton.hasClass 'show'
			console.log 'listSelectItem...', id, value, name

	removeItem: ->
		col = db.get 'environmentsStatic'
		index = col.indexOf @selectedItem
		EditorEventbus.removeStaticObject.dispatch @selectedItem
		# TODO: select next item after removing current one
		if col.length > index
			@selectedItem = col.at index
		else if index > 0
			@selectedItem = col.at index - 1
		else
			@selectedItem = null
		@$("div[data-value='#{@selectedItem.get('id')}']").addClass 'active' if @selectedItem
		@$('#sidebar-pathing-remove').removeClass 'show' unless @selectedItem

return Environment