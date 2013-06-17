EditorRouter = require 'editorRouter'

app =
	views: {}
	
	start: ->
		app.router = new EditorRouter()
		Backbone.history.start pushState: true

return app
