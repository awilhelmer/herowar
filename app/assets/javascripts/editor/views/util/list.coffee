BaseView = require 'views/baseView'
templates = require 'templates'

class List extends BaseView

	className: 'list'
		
	template: templates.get 'util/list.tmpl'

	initialize: (options) ->
		@entity = @$el.data 'entity'
		@$el.removeAttr 'data-entity'
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	render: ->
		console.log 'Render List...'
		console.log @getTemplateData()
		super()

return List