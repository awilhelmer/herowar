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
		EditorEventbus.listSelectItem.add @listSelectItem

	initialize: (options) ->
		@selectedItem = null
		super options

	listSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-list'
			@selectedItem = db.get 'environmentsStatic', value
			$removeButton = @$ '#sidebar-environment-remove'
			$removeButton.addClass 'show' unless $removeButton.hasClass 'show'

	removeItem: ->
		col = db.get 'environmentsStatic'
		index = col.indexOf @selectedItem
		EditorEventbus.dispatch 'removeStaticObject', @selectedItem
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