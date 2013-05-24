events = require 'events'

class PacketModel extends Backbone.Model

	type: null
	
	timeValues: {}

	rate: 1000
	
	defaultValues:
		'_active' : false

	initialize: (options) ->
		super options
		@setDefaultValues()
		@bindPacketEvents()
	
	update: =>
		@updateTimeValue key, val for key, val of @timeValues if @get('_active') and not @has '_freeze'
	
	setDefaultValues: ->
		@set @defaultValues
	
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
				@calculateRate()
				@start() if @rate > 0
			@trigger 'change'

	updateTimeValue: (valueKey, incrementKey, timeKey) ->
		#console.log 'UpdateTimeValue', valueKey, @get(valueKey), incrementKey, @get(incrementKey)
		unless @get(valueKey) or @get(incrementKey) then return
		unless timeKey
			timeKey = if not @get('updateTime') or @get('updateTime') < @get('retrieveTime') then 'retrieveTime' else 'updateTime'
			#console.log 'Set timekey: ', timeKey
		time = (new Date()).getTime()
		dif = time - @get timeKey
		newVal = Math.round(@get(valueKey) + (dif / 1000 * @get incrementKey))
		update = {}
		update[valueKey] = newVal
		update.updateTime = time	
		@set update

	calculateRate: ->
		newRate = @rate
		for key, val of @timeValues
			if @get(key) or @get(val)
				currentRate = Math.round 1000 / @get val
				#console.log 'Calculate rate for', key, ':', currentRate
				newRate = currentRate if currentRate < newRate
		if newRate isnt @rate
			console.log 'Update rate ', newRate, ' from ', @rate
			@rate = newRate
	
return PacketModel