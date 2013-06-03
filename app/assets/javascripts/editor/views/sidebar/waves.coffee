BasePropertiesView = require 'views/basePropertiesView'
EditorEventbus = require 'editorEventbus'
Wave = require 'models/db/wave'
templates = require 'templates'
log = require 'util/logger'
db = require 'database'

class Waves extends BasePropertiesView

	id: 'sidebar-waves'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/waves.tmpl'

	events:
		'click #sidebar-waves-create'	: 'createItem'
		'click #sidebar-waves-remove'	: 'removeItem'

	bindEvents: ->
		EditorEventbus.listSelectItem.add @listSelectItem
		EditorEventbus.initIdChanged.add @setStartId

	initialize: (options) ->
		@paths = db.get 'db/paths'
		@nextId = 1
		@nextDbId = -1
		@selectedItem = null
		super options

	listSelectItem: (id, value, name) =>
		if id is 'sidebar-waves-list'
			@selectedItem = db.get 'db/waves', value
			$removeButton = @$ '#sidebar-waves-remove'
			$removeButton.addClass 'show' unless $removeButton.hasClass 'show'

	createItem: ->
		id = @nextId++
		dbId = @nextDbId--
		wave = new Wave()
		wave.set 
			'id'		: id
			'dbId'	: dbId
			'name' 	: "Wave-#{id}"
		wave.set 'path', @paths.models[0].get('id') if @paths.length > 0
		col = db.get 'db/waves'
		col.add wave
		log.info "Wave \"#{wave.get('name')}\" created"

	removeItem: ->
		col = db.get 'db/waves'
		index = col.indexOf @selectedItem
		col.remove @selectedItem
		log.info "Wave \"#{@selectedItem.get('name')}\" removed"
		if col.length > index
			@selectedItem = col.at index
		else if index > 0
			@selectedItem = col.at index - 1
		else
			@selectedItem = null
		@$("div[data-value='#{@selectedItem.get('id')}']").addClass 'active' if @selectedItem
		@$('#sidebar-waves-remove').removeClass 'show' unless @selectedItem

	setStartId: (module, startId) =>
		if module is 'waves'
			log.info "Setting Start Id of waves to #{startId}"
			@nextId = startId

return Waves