events = require 'events'

class Preload extends Backbone.Model

	initialize: (options) ->
		super options
		@bindEvents()

	bindEvents: ->
		events.on 'retrieve:packet:50', @updateAttributes, @
		
	updateAttributes: (packet) ->
		if packet
			_.extend @attributes, _.omit packet, 'type', 'createdTime'
			@trigger 'change'

return Preload