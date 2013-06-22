SocketClient = require 'network/socketclient'
GameRouter = require 'gameRouter'
db = require 'database'

app =
	views: {}
	
	history: []
	
	start: ->
		app.socketClient = new SocketClient()
		app.router = new GameRouter()
		Backbone.history.start pushState: true
		@initModels()

	# We have to initialize models to deal with init packets before the view is created
	initModels: ->
		db.get 'ui/enemies'
		db.get 'ui/preload'
		db.get 'ui/stats'
		db.get 'ui/waves'
		settings = db.get 'db/settings'
		settings.fetch()

return app