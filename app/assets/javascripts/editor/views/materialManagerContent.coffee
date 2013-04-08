BaseView = require 'views/baseView'
templates = require 'templates'

class MaterialManagerContent extends BaseView

	id: 'materialManagerContent'

	entity: 'materials'

	template: templates.get 'materialManagerContent.tmpl'
	
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model
	
	render: ->
		console.log 'Render material manager content'
		console.log @getTemplateData()
		super()

return MaterialManagerContent