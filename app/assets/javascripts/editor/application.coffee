Router = require 'router'

app =
	resourcePath: -> "http://localhost:9000/api/"	

	views: {}
	
	start: ->
		app.router = new Router()
		Backbone.history.start pushState: true

return app
