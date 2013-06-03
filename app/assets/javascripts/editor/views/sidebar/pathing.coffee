BasePropertiesView = require 'views/basePropertiesView'
EditorEventbus = require 'editorEventbus'
Path = require 'models/db/path'
templates = require 'templates'
log = require 'util/logger'
db = require 'database'

class Pathing extends BasePropertiesView

	id: 'sidebar-pathing'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/pathing.tmpl'

	events:
		'click #sidebar-pathing-create'	: 'createItem'
		'click #sidebar-pathing-remove'	: 'removeItem'

	bindEvents: ->
		EditorEventbus.listSelectItem.add @listSelectItem
		EditorEventbus.initIdChanged.add @setStartId

	initialize: (options) ->
		@waves = db.get 'db/waves'
		@nextId = 1
		@nextDbId = -1
		@selectedItem = null
		super options

	listSelectItem: (id, value, name) =>
		if id is 'sidebar-pathing-list'
			@selectedItem = db.get 'db/paths', value
			$removeButton = @$ '#sidebar-pathing-remove'
			$removeButton.addClass 'show' unless $removeButton.hasClass 'show'

	createItem: ->
		id = @nextId++
		dbId = @nextDbId--
		path = new Path()
		path.set 
			'id'		: id
			'dbId'	: dbId
			'name' 	: "Path-#{id}"
		col = db.get 'db/paths'
		col.add path
		log.info "Path \"#{path.get('name')}\" created"
		@addPathToIncompleteWaves id

	addPathToIncompleteWaves: (id) ->
		for wave in @waves.models
			wave.set 'path', id unless wave.has('path')

	removeItem: ->
		col = db.get 'db/paths'
		index = col.indexOf @selectedItem
		col.remove @selectedItem
		log.info "Path \"#{@selectedItem.get('name')}\" removed"
		if col.length > index
			@selectedItem = col.at index
		else if index > 0
			@selectedItem = col.at index - 1
		else
			@selectedItem = null
		@$("div[data-value='#{@selectedItem.get('id')}']").addClass 'active' if @selectedItem
		@$('#sidebar-pathing-remove').removeClass 'show' unless @selectedItem

	setStartId: (module, startId) =>
		if module is 'pathing'
			log.info "Setting Start Id of pathing to #{startId}"
			@nextId = startId
			
return Pathing