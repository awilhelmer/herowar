BasePropertiesView = require 'views/basePropertiesView'
EditorEventbus = require 'editorEventbus'
Wave = require 'models/wave'
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

	initialize: (options) ->
		@nextId = 1
		@selectedItem = null
		super options

	listSelectItem: (id, value, name) =>
		if id is 'sidebar-waves-list'
			@selectedItem = db.get 'waves', value
			$removeButton = @$ '#sidebar-waves-remove'
			$removeButton.addClass 'show' unless $removeButton.hasClass 'show'

	createItem: ->
		id = @nextId++
		wave = new Wave()
		wave.set 
			'id'		: id
			'name' 	: "Wave-#{id}"
		col = db.get 'waves'
		col.add wave
		log.info "Wave \"#{wave.get('name')}\" created"

	removeItem: ->
		col = db.get 'waves'
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

return Waves