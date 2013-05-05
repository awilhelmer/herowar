EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class List extends BaseView

	className: 'list'
		
	template: templates.get 'util/list.tmpl'

	events:
		'click .item'	: 'selectElement'

	initialize: (options) ->
		@entity = @$el.data 'entity'
		@selectedVal = @$el.data 'selected'
		@$el.removeAttr 'data-entity data-selected'
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model
		EditorEventbus.listSetItem.add @setItem

	setItem: (id, value) =>
		@selectedVal = value if id is @$el.attr 'id'

	selectElement: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		@$('.item').removeClass 'active'
		$currentTarget.addClass 'active'
		@selectedVal = $currentTarget.data('value')
		EditorEventbus.dispatch 'listSelectItem', @$el.attr('id'), @selectedVal, $currentTarget.data('name')

	render: ->
		super()
		@$("div[data-value='#{@selectedVal}']").addClass 'active' if @selectedVal

return List