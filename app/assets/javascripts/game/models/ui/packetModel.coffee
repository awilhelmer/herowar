events = require 'events'

class PacketModel extends Backbone.Model

	types: null
	
	timeValues: {}

	rate: 1000

	defaultValues:
		_active : false

	initialize: (options) ->
		super options
		@setDefaultValues()
		@bindPacketEvents()
		return
	
	update: =>
		@updateTimeValue key, val for key, val of @timeValues if @get('_active') and not @has '_freeze'
		return
	
	setDefaultValues: ->
		@set @defaultValues
		return
	
	bindPacketEvents: ->
		if not _.isNull(@types) and _.isArray @types
			@bindPacketEvent t for t in @types
		else if not _.isNull @types
			@bindPacketEvent @types
		return
	
	bindPacketEvent: (type) ->
		events.on "retrieve:packet:#{type}", @onPacket, @
		return
	
	start: ->
		@interval = setInterval @update, @rate unless @interval
		return
		
	stop: ->
		if @interval
			clearInterval @interval
			delete @interval
			return

	onPacket: (packet) ->
		values = _.extend _.omit(packet, 'type', 'createdTime'), { retrieveTime: packet.createdTime }
		delete values[key] for own key, value of values when value is null
		_.extend @attributes, values
		unless @get '_active'
			_.extend @attributes, { _active : true }
			@calculateRate()
			@start() if @rate > 0
		@trigger "change:#{key}" for own key, value of values
		@trigger 'change'
		return

	updateTimeValue: (valueKey, incrementKey, timeKey) ->
		unless @get(valueKey) or @get(incrementKey) then return
		unless timeKey
			timeKey = if not @get('updateTime') or @get('updateTime') < @get('retrieveTime') then 'retrieveTime' else 'updateTime'
		time = (new Date()).getTime()
		dif = time - @get timeKey
		newVal = Math.round(@get(valueKey) + (dif / 1000 * @get incrementKey))
		update = {}
		update[valueKey] = newVal
		update.updateTime = time	
		@set update
		return

	calculateRate: ->
		newRate = @rate
		for key, val of @timeValues
			if @get(key) or @get(val)
				currentRate = Math.round 1000 / @get val
				newRate = currentRate if currentRate < newRate
		if newRate isnt @rate
			console.log 'Update rate ', newRate, ' from ', @rate
			@rate = newRate
		return
	
return PacketModel