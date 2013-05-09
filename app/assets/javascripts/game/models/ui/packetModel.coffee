events = require 'events'

class PacketModel extends Backbone.Model

	type: null
	
	timeValues: {}

	rate: 1000

	initialize: (options) ->
		super options
		@setDefaultValues()
		@bindPacketEvents()
	
	update: =>
		@updateTimeValue key, val for key, val of @timeValues
	
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
			_.extend @attributes, _.extend _.omit(packet, 'type', 'createdTime'), { retrieveTime: packet.createdTime }
			unless @get '_active'
				_.extend @attributes, { _active : true }
				@start() if @rate > 0
			@trigger 'change'

	updateTimeValue: (valueKey, incrementKey, timeKey) ->
		console.log 'UpdateTimeValue', valueKey, @get(valueKey), timeKey, @get(timeKey), incrementKey, @get(incrementKey)
		unless @get(valueKey) or @get(incrementKey) then return
		unless timeKey
			timeKey = if not @get('updateTime') or @get('updateTime') < @get('retrieveTime') then 'retrieveTime' else 'updateTime'
			console.log 'Set timekey: ', timeKey
		time = (new Date()).getTime()
		dif = time - @get timeKey
		newVal = Math.round(@get(valueKey) + ((dif / 1000) * @get incrementKey))
		update = {}
		update[valueKey] = newVal
		update.updateTime = time	
		@set update
	
return PacketModel