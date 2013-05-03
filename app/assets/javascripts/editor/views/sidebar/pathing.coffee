BasePropertiesView = require 'views/basePropertiesView'
EditorEventbus = require 'editorEventbus'
Path = require 'models/path'
templates = require 'templates'
db = require 'database'

class Pathing extends BasePropertiesView

	id: 'sidebar-pathing'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/pathing.tmpl'

	events:
		'click #sidebar-pathing-create'	: 'createItem'
		'click #sidebar-pathing-remove'	: 'removeItem'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @hidePanel
		EditorEventbus.showSidebarPathing.add @showPanel
		EditorEventbus.showPathingProperties.add @hidePanel
		EditorEventbus.listSelectItem.add @listSelectItem

	initialize: (options) ->
		@nextId = 1
		@selectedItem = null
		super options

	listSelectItem: (id, value, name) =>
		if id is 'sidebar-pathing-list'
			@selectedItem = db.get 'paths', value
			$removeButton = @$ '#sidebar-pathing-remove'
			$removeButton.addClass 'show' unless $removeButton.hasClass 'show'
			console.log 'listSelectItem...', id, value, name

	createItem: ->
		id = @nextId++
		path = new Path()
		path.set 
			'id'		: id
			'name' 	: "Path-#{id}"
		col = db.get 'paths'
		col.add path

	removeItem: ->
		col = db.get 'paths'
		col.remove @selectedItem
		# TODO: select next item after removing current one
		@selectedItem = null
		@$('#sidebar-pathing-remove').removeClass 'show'

return Pathing