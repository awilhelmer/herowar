EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
Constants = require 'constants'
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
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		$('#materialManager .mm-material').removeClass 'active'
		$currentTarget.addClass 'active'
		modelId = $currentTarget.data 'matid'
		@dispatchSelectMaterialEvent modelId if modelId
						
	dispatchSelectMaterialEvent: (modelId) ->
		EditorEventbus.menuSelectMaterial.dispatch modelId
		EditorEventbus.showMaterialProperties.dispatch()

return MaterialManagerContent