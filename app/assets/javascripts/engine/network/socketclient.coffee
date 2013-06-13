BasePacket = require 'network/packets/basePacket'
log = require 'util/logger'
events = require 'events'

class SocketClient

	constructor: ->
		throw 'Oh no, you need a browser that supports WebSockets. How about Google Chrome?' unless _.has window, 'WebSocket'
		host = 'ws://localhost:9005/'
		@socket = new WebSocket host
		@isOpen = false
		@isAuthenticated = false
		log.debug "Creating web socket connection to #{host}"
		@bindEvents()
		
	bindEvents: ->
		@socket.onopen = @onOpen
		@socket.onclose = @onClose
		@socket.onmessage = @onMessage
		@socket.onerror = @onError
		events.on 'send:packet', @send, @

	onOpen: (event) =>
		log.debug "Socket Status: #{@socket.readyState} (Opened)"
		@isOpen = true
	
	onClose: (event) =>
		log.debug "Socket Status: #{@socket.readyState} (Closed)"
		@isOpen = false
		@isAuthenticated = false

	onMessage: (event) =>
		if event and event.data
			packet = JSON.parse event.data
			if packet.type
				log.debug "[SocketClient] Trigger 'retrieve:packet:#{packet.type}' event for packet '#{event.data}'"
				events.trigger "retrieve:packet:#{packet.type}", packet

	onError: (event) =>
		throw 'Oh no, an error occured in socket client'

	send: (packet) ->
		@socket.send packet.get() if @isOpen and packet instanceof BasePacket

return SocketClient