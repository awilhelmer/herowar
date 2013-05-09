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
		if not _.isNull(@type) and _.isArray @type
			@bindPacketEvent t for t in @type
		else if not _.isNull @type
			@bindPacketEvent @type
	
	bindPacketEvent: (type) ->
		events.on "retrieve:packet:#{type}", @onPacket, @
	
	start: ->
		@interval = setInterval @update, @rate unless @interval
		
	stop: ->
		if @interval
			clearInterval @interval
			delete @interval		

	onPacket: (packet) ->
		if packet
			_.extend @attributes, _.omit packet, 'type', 'createdTime'
			unless @get '_active'
				_.extend @attributes, { _active : true }
				@start() if @rate > 0
			@trigger 'change'
	
return PacketModel