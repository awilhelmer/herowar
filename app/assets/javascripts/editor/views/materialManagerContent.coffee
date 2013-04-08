EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class MaterialManagerContent extends BaseView

	id: 'materialManagerContent'

	entity: 'materials'

	template: templates.get 'materialManagerContent.tmpl'

	events:
		'click .mm-material' : 'loadMaterial'
	
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model
	
	loadMaterial: (event) =>
		event?.preventDefault()
		console.log 'Load material'
		EditorEventbus.showMaterialProperties.dispatch()

return MaterialManagerContent