ChatMessagePacket = require 'network/packets/chatMessagePacket'
BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'

class ChatView extends BaseView

	id: 'chat'
		
	template: templates.get 'chat.tmpl'

	entity: 'ui/chat'
	
	events:
		'click .send' : 'sendMessage'
		
	sendMessage: (event) ->
		$input = @$el.find 'input'
		console.log 'Send', $input.val()
		message = $input.val()
		if message
			events.trigger 'send:packet', new ChatMessagePacket message
		return

return ChatView