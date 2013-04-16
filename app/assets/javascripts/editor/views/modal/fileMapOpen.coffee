BaseModalView = require 'views/baseModalView'
templates = require 'templates'
db = require 'database'

class ModalFileMapOpen extends BaseModalView

	id: 'modalFileMapOpen'
	
	className: 'modal hide fade'
	
	entity: 'maps'
		
	template: templates.get 'modal/fileMapOpen.tmpl'

	events:
		'click .btn-primary' : 'mapOpen'

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model
	
	initialize: (options) ->
		super options
		@model.fetch()

	mapOpen: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		$currentTarget.popover
			placement : 'top'
			title 		: 'Error'
			content		: 'Please choose a map above.'

return ModalFileMapOpen