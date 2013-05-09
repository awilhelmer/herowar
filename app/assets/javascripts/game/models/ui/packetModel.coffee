events = require 'events'

class PacketModel extends Backbone.Model

	type: null

	rate: 1000

	initialize: (options) ->
		super options
		@setDefaultValues()
		@bindPacketEvents()
	
	# Should be overridden
	update: ->
	
	setDefaultValues: ->
		@set
			'_active' : false
	
	bindPacketEvents: ->
		events.on "retrieve:packet:#{@type}", @onPacket, @ if @type
		
	start: ->
		@interval = setInterval @update, @rate unless @interval and not @rate
		
	stop: ->
		if @interval
			clearInterval @interval
			delete @interval		

	onPacket: (packet) ->
		if packet
			_.extend @attributes, _.omit packet, 'type', 'createdTime'
			unless @get '_active'
				_.extend @attributes, { _active : true }
				@start()
			@trigger 'change'
	
return PacketModel