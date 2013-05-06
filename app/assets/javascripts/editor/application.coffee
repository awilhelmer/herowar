EditorRouter = require 'editorRouter'

app =
	resourcePath: -> "http://localhost:9000/api/"	

	views: {}
	
	start: ->
		app.router = new EditorRouter()
		Backbone.history.start pushState: true

return app
