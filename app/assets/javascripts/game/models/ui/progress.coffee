events = require 'events'

class Progress extends Backbone.Model

	initialize: (options) ->
		super options
		@setDefaultValues()
		@bindEvents()
	
	# TODO: default values should come server ...
	setDefaultValues: ->
		@set
			'lives' 			: 0
			'gold' 				: 0
			'waveCurrent'	: 0
			'waveCount'		: 0

	bindEvents: ->
		events.on 'retrieve:packet:35', @updateAttributes, @
		
	updateAttributes: (packet) ->
		if packet
			_.extend @attributes, _.omit packet, 'type', 'createdTime'
			@trigger 'change'

return Progress