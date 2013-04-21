EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class Tree extends BaseView

	className: 'tree'
		
	template: templates.get 'util/tree.tmpl'

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
			console.log "Load tree data for #{id}"
			@loadedData = true
			@model.fetch()

	render: ->
		console.log 'Render Tree...'
		console.log @getTemplateData()
		super()

return Tree