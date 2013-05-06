GameRouter = require 'gameRouter'

app =
	resourcePath: -> "http://localhost:9000/api/"	

	views: {}
	
	start: ->
		app.router = new GameRouter()
		Backbone.history.start pushState: true

return app