EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
log = require 'util/logger'

class Tree extends BaseView

	className: 'tree'
		
	template: templates.get 'util/tree.tmpl'
	
	events:
		'click .item'	: 'selectElement'

	initialize: (options) ->
		@entity = @$el.data 'entity'
		@$el.removeAttr 'data-entity'
		@loadedData = false
		super options

	bindEvents: ->
		EditorEventbus.treeLoadData.add @loadData
		super()
	
	loadData: (id) =>
		if not @loadedData and @$el.attr('id') is id
			log.debug "Load tree data for #{id}"
			@loadedData = true
			@model.fetch()

	selectElement: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		@$('.item').removeClass 'active'
		$currentTarget.addClass 'active'
		EditorEventbus.dispatch 'treeSelectItem', @$el.attr('id'), $currentTarget.data('value')

return Tree