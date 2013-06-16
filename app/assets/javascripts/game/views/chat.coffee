ChatMessagePacket = require 'network/packets/chatMessagePacket'
BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'
db = require 'database'

class ChatView extends BaseView

	id: 'chat'
		
	template: templates.get 'chat.tmpl'

	entity: 'ui/chat'
	
	events:
		'click .send' : 'sendMessage'

	initialize: (options) ->
		super options
		world = db.get 'world'
		@$el.addClass 'hidden' if world.get('name') is 'Tutorial'
		return
		
	sendMessage: (event) ->
		$input = @$el.find 'input'
		console.log 'Send', $input.val()
		message = $input.val()
		if message
			events.trigger 'send:packet', new ChatMessagePacket message
		return

return ChatView