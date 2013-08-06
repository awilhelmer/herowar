BasePacket = require 'network/packets/basePacket'
log = require 'util/logger'
events = require 'events'

class SocketClient

	constructor: ->
		throw 'Oh no, you need a browser that supports WebSockets. How about Google Chrome?' unless _.has window, 'WebSocket'
		port = if location.port then ":#{location.port}" else ''
		host = "ws://#{location.hostname}#{port}/game/data"
		@socket = new WebSocket host
		@isOpen = false
		@isAuthenticated = false
		@appActive = false
		@packetsOnHold = []
		log.debug "Creating web socket connection to #{host}"
		@bindEvents()
		
	bindEvents: ->
		@socket.onopen = @onOpen
		@socket.onclose = @onClose
		@socket.onmessage = @onMessage
		@socket.onerror = @onError
		events.on 'created:controller:application', @applicationInitialized, @
		events.on 'send:packet', @send, @
		return

	onOpen: (event) =>
		log.debug "Socket Status: #{@socket.readyState} (Opened)"
		@isOpen = true
		return
	
	onClose: (event) =>
		log.debug "Socket Status: #{@socket.readyState} (Closed)"
		@isOpen = false
		@isAuthenticated = false
		Backbone.history.loadUrl 'game/interrupted'
		return

	onMessage: (event) =>
		if event and event.data
			packet = JSON.parse event.data
			if (packet.type and @appActive) or (packet.type is 11 || packet.type is 12 || packet.type is 13 || packet.type is 35 || packet.type is 50 ||  packet.type is 60)
				#log.debug "[SocketClient] Trigger 'retrieve:packet:#{packet.type}' event for packet '#{event.data}'"
				events.trigger "retrieve:packet:#{packet.type}", packet
			else
				@packetsOnHold.push packet
		return

	onError: (event) =>
		throw 'Oh no, an error occured in socket client'
		return

	send: (packet) ->
		@socket.send packet.get() if @isOpen and packet instanceof BasePacket
		return

	applicationInitialized: ->
		console.log "Application initialized working on #{@packetsOnHold.length} delayed packets"
		@appActive = true
		for packet in @packetsOnHold
			log.debug "[SocketClient] Trigger 'retrieve:packet:#{packet.type}' event for packet '#{JSON.stringify(packet)}'"
			events.trigger "retrieve:packet:#{packet.type}", packet
		@packetsOnHold.length = 0
		return

return SocketClient