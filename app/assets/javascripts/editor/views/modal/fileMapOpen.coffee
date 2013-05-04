BaseModalView = require 'views/baseModalView'
templates = require 'templates'
app = require 'application'
log = require 'util/logger'
db = require 'database'

class ModalFileMapOpen extends BaseModalView

	id: 'modalFileMapOpen'
	
	className: 'modal hide fade'
	
	entity: 'maps'
		
	template: templates.get 'modal/fileMapOpen.tmpl'

	events:
		'click .modal-body div'	: 'chooseMap'
		'click .btn-primary' 		: 'mapOpen'

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	initialize: (options) ->
		@mapId = -1
		@addPopover()
		super options
		@model.fetch()

	addPopover: ->
		$('.btn-primary').popover
			placement : 'left'
			trigger		: 'hover'
			title 		: 'Error'
			content		: 'Please choose a map above.'		

	destroyPopover: ->
		$('.btn-primary').popover 'destroy'

	chooseMap: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		mapId = $currentTarget.data 'mapid'
		unless mapId then return
		@mapId = mapId
		@destroyPopover()
		$('.modal-body div').removeClass 'active'
		$currentTarget.addClass 'active'

	mapOpen: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		if @mapId is -1
			$currentTarget.popover 'show'
		else
			log.debug "Choose map #{@mapId}"
			window.location = "/editor?map=#{@mapId}"

return ModalFileMapOpen