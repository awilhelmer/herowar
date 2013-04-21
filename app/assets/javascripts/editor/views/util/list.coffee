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
		@$el.removeAttr 'data-entity'
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	selectElement: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		@$('.item').removeClass 'active'
		$currentTarget.addClass 'active'
		EditorEventbus.listSelectItem.dispatch @$el.attr('id'), $currentTarget.data('value')

	render: ->
		console.log 'Render List...'
		console.log @getTemplateData()
		super()

return List