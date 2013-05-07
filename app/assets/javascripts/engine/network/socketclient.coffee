log = require 'util/logger'

class SocketClient

	constructor: ->
		throw 'Oh no, you need a browser that supports WebSockets. How about Google Chrome?' unless _.has window, 'WebSocket'
		host = 'ws://localhost:8081/'
		@socket = new WebSocket host
		@isOpen = false
		@isAuthenticated = false
		log.info "Creating web socket connection to #{host}"
		@bindEvents()
		
	bindEvents: ->
		@socket.onopen = @onOpen
		@socket.onclose = @onClose
		@socket.onmessage = @onMessage
		@socket.onerror = @onError

	onOpen: (event) =>
		log.info "Socket Status: #{@socket.readyState} (Opened)"
	
	onClose: (event) =>
		log.info "Socket Status: #{@socket.readyState} (Closed)"

	onMessage: (event) =>
		console.log '[SocketClient] Received: ', event

	onError: (event) =>
		throw 'Oh no, an error occured in socket client'

return SocketClient