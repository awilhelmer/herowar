BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'

class ChatView extends BaseView

	id: 'chat'
		
	template: templates.get 'chat.tmpl'

	entity: 'ui/chat'

return ChatView