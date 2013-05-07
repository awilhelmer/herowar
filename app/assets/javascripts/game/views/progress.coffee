BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'

class ProgressView extends BaseView

	id: 'progress'
	
	entity: 'ui/progress'
	
	template: templates.get 'progress.tmpl'
	
	initialize: (options) ->
		super options
		# TODO: default values should come server ...
		@model.set
			'lives' 			: 0
			'gold' 				: 0
			'waveCurrent'	: 1
			'waveCount'		: 15

	bindEvents: ->
		events.on 'retrieve:packet:35', @updateModel, @
		super()
		
	updateModel: (packet) ->
		@model.set
			'lives' : packet.lives
			'gold'	: packet.gold
	
return ProgressView